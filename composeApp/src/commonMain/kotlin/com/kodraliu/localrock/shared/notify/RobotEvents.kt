package com.kodraliu.localrock.shared.notify

import com.kodraliu.localrock.shared.vacuum.DockErrorCodes
import com.kodraliu.localrock.shared.vacuum.VacuumErrorCodes
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import com.kodraliu.localrock.shared.vacuum.VacuumStatus
import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.joinedText
import com.kodraliu.localrock.shared.text.rawText
import com.kodraliu.localrock.shared.text.resolve
import com.kodraliu.localrock.shared.text.uiText

/** Something about a robot that is worth interrupting someone for. */
sealed interface RobotEvent {
    /** The robot raised an error: stuck, brush blocked, and so on. */
    data class Error(val code: Int) : RobotEvent

    /** A clean run ended with the robot back on its dock. */
    data class CleanFinished(val areaMm2: Long?, val seconds: Long?) : RobotEvent

    /** The dock needs attention: water empty, dirty tank full, and so on. */
    data class DockAlert(val code: Int) : RobotEvent
}

/**
 * Turns a stream of status snapshots into [RobotEvent]s. Edge-triggered: a condition is reported
 * when it appears, not for as long as it lasts. A field the robot has not reported yet is unknown
 * rather than "fine", so whatever is already wrong when watching starts is the baseline and is
 * not announced.
 */
/**
 * The last alert code announced per kind ("robot", "dock") for one robot, so a problem is announced
 * once even across restarts of the app, and again only after it cleared.
 */
interface AlertMemory {
    fun announced(kind: String): Int?
    fun remember(kind: String, code: Int?)
}

class InMemoryAlertMemory : AlertMemory {
    private val codes = mutableMapOf<String, Int?>()
    override fun announced(kind: String): Int? = codes[kind]
    override fun remember(kind: String, code: Int?) { codes[kind] = code }
}

class RobotEventDetector(private val memory: AlertMemory = InMemoryAlertMemory()) {
    private var last = VacuumStatus()

    /**
     * True from the moment a clean run is seen until it ends, through the returning, washing and
     * recharging in between. This is the span a progress display should cover.
     */
    var runInProgress = false
        private set

    fun update(next: VacuumStatus): List<RobotEvent> {
        val prev = last
        last = next
        val events = mutableListOf<RobotEvent>()

        // A problem is announced once: also one that was already there when watching started (a dock
        // with an empty water tank said nothing otherwise), and again only after the robot cleared it.
        // Only with a known state, so a lone push before the first status is not taken for news.
        if (next.state != null) {
            alert("robot", next.errorCode) { events += RobotEvent.Error(it) }
            alert("dock", next.dockErrorStatus) { events += RobotEvent.DockAlert(it) }
        }

        if (next.state in CLEANING_STATES) {
            runInProgress = true
        } else if (runInProgress && next.jobOver && next.state in RESTING_STATES) {
            runInProgress = false
            // Stopped out on the floor, or carried home with an error, is over but not finished.
            if (next.state in DOCKED_STATES && (next.errorCode ?: 0) == 0) {
                events += RobotEvent.CleanFinished(next.cleanArea, next.cleanTime)
            }
        }
        return events
    }

    private inline fun alert(kind: String, code: Int?, announce: (Int) -> Unit) {
        if (code == null) return
        if (code == 0) {
            if (memory.announced(kind) != null) memory.remember(kind, null)
            return
        }
        if (memory.announced(kind) == code) return
        memory.remember(kind, code)
        announce(code)
    }

    private companion object {
        /**
         * Only these end a run. Returning, washing the mop and emptying the bin all happen in the
         * middle of one as well.
         */
        val DOCKED_STATES = setOf(VacuumStateCodes.CHARGING, VacuumStateCodes.CHARGING_COMPLETE)
        val RESTING_STATES = DOCKED_STATES + setOf(VacuumStateCodes.IDLE, VacuumStateCodes.NOT_CHARGING)
    }
}

internal val CLEANING_STATES = setOf(
    VacuumStateCodes.CLEANING,
    VacuumStateCodes.SPOT_CLEANING,
    VacuumStateCodes.ZONED_CLEANING,
    VacuumStateCodes.SEGMENT_CLEANING,
)

/**
 * in_cleaning stays non-zero while the robot recharges or washes its mop partway through a job,
 * which is what tells a break from the end. A robot that does not report it is taken at its word
 * once it is charging.
 */
private val VacuumStatus.jobOver: Boolean get() = (inCleaning ?: 0) == 0

/** What is shown to the user for one event, wherever it ends up being shown. */
data class RobotNotification(
    val duid: String,
    val title: String,
    val body: String,
    /** True for something that needs a person, false for good news. */
    val alert: Boolean,
)

fun RobotEvent.toNotification(duid: String, deviceName: String): RobotNotification =
    RobotNotification(duid, deviceName, notificationBody().english, alert = this !is RobotEvent.CleanFinished)

/** [toNotification] in the app's language. */
suspend fun RobotEvent.localizedNotification(duid: String, deviceName: String): RobotNotification =
    RobotNotification(duid, deviceName, notificationBody().resolve(), alert = this !is RobotEvent.CleanFinished)

/** What a notification about this event says. */
fun RobotEvent.notificationBody(): UiText = when (this) {
    is RobotEvent.Error -> VacuumErrorCodes.text(code)
    is RobotEvent.DockAlert -> DockErrorCodes.text(code)
    is RobotEvent.CleanFinished -> finishedText(areaMm2, seconds)
}

private fun finishedText(areaMm2: Long?, seconds: Long?): UiText {
    val area = formatArea(areaMm2)
    val time = formatMinutes(seconds)
    val parts = listOfNotNull(area, time)
    val detail = when {
        area != null && time != null ->
            uiText(Res.string.notify_area_in_time, "${area.english} in ${time.english}", area, time)
        else -> parts.singleOrNull()
    }
    return if (detail == null) {
        uiText(Res.string.notify_cleaning_finished, "Cleaning finished")
    } else {
        uiText(Res.string.notify_cleaning_finished_detail, "Cleaning finished: ${detail.english}", detail)
    }
}

/** A running clean job, for a display that stays up and is updated as the job goes on. */
data class RobotProgress(
    val duid: String,
    /** The robot and what it is doing, for example "S8: Cleaning in Kitchen". */
    val title: String,
    /** The numbers, for example "42% · 12.5 m² · 18 min · Battery 64%". */
    val detail: String,
    /** 0..100, or null when the robot does not report how far along it is. */
    val percent: Int?,
)

/** [room] is where the robot is now, when that is known; it is only shown while it is cleaning. */
fun progressOf(duid: String, deviceName: String, status: VacuumStatus, room: String?): RobotProgress {
    val (title, detail) = progressText(deviceName, status, room)
    return RobotProgress(duid, title.english, detail.english, status.cleanPercent?.coerceIn(0, 100))
}

/** [progressOf] in the app's language. */
suspend fun localizedProgressOf(duid: String, deviceName: String, status: VacuumStatus, room: String?): RobotProgress {
    val (title, detail) = progressText(deviceName, status, room)
    return RobotProgress(duid, title.resolve(), detail.resolve(), status.cleanPercent?.coerceIn(0, 100))
}

/** The title and detail lines of [progressOf]. */
private fun progressText(deviceName: String, status: VacuumStatus, room: String?): Pair<UiText, UiText> {
    val error = status.errorCode ?: 0
    val activity = when {
        error != 0 -> VacuumErrorCodes.text(error)
        else -> status.state?.let { VacuumStateCodes.text(it) } ?: uiText(Res.string.notify_working, "Working")
    }
    val where = room?.takeIf { error == 0 && status.state in CLEANING_STATES }
    val percent = status.cleanPercent?.coerceIn(0, 100)
    val detail = joinedText(listOf(
        percent?.let { rawText("$it%") },
        formatArea(status.cleanArea),
        formatMinutes(status.cleanTime),
        status.battery?.let { uiText(Res.string.notify_battery, "Battery $it%", it) },
    )) ?: rawText("")
    val title = if (where == null) {
        uiText(Res.string.notify_progress_title, "$deviceName: ${activity.english}", deviceName, activity)
    } else {
        uiText(Res.string.notify_progress_title_room, "$deviceName: ${activity.english} in $where", deviceName, activity, where)
    }
    return title to detail
}

/** mm² to "12.5 m²"; null when there is nothing to show yet. */
private fun formatArea(areaMm2: Long?): UiText? {
    if (areaMm2 == null || areaMm2 <= 0) return null
    val tenths = (areaMm2 + 50_000) / 100_000
    return uiText(Res.string.notify_area, "${tenths / 10}.${tenths % 10} m²", tenths / 10, tenths % 10)
}

private fun formatMinutes(seconds: Long?): UiText? =
    if (seconds == null || seconds <= 0) null else ((seconds + 30) / 60).let { uiText(Res.string.duration_minutes, "$it min", it) }
