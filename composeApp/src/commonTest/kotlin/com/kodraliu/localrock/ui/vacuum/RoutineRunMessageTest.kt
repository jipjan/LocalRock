package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.robot_routine_not_started_status
import com.kodraliu.localrock.resources.robot_routine_started
import com.kodraliu.localrock.resources.robot_routine_still_running
import com.kodraliu.localrock.resources.robot_routine_stopping
import com.kodraliu.localrock.shared.model.SceneRunResult
import kotlin.test.Test
import kotlin.test.assertEquals

// The texts are string resources (English: values/strings_robot.xml), which a JVM unit test can't
// read, so these check which message is picked and what goes into it.
class RoutineRunMessageTest {

    @Test
    fun a_started_routine_says_so() {
        assertEquals(
            RoutineRunText(true, Res.string.robot_routine_started, listOf("Kitchen first")),
            routineRunText("Kitchen first", SceneRunResult(status = "started")),
        )
    }

    @Test
    fun running_the_active_routine_again_stops_it() {
        assertEquals(
            RoutineRunText(true, Res.string.robot_routine_stopping, listOf("Kitchen first")),
            routineRunText("Kitchen first", SceneRunResult(status = "cancel_requested")),
        )
    }

    @Test
    fun another_running_routine_is_named_in_the_error() {
        assertEquals(
            RoutineRunText(false, Res.string.robot_routine_still_running, listOf("Whole home")),
            routineRunText(
                "Kitchen first",
                SceneRunResult(accepted = false, status = "routine_in_progress", activeSceneName = "Whole home"),
            ),
        )
    }

    @Test
    fun a_refusal_names_the_routine_and_the_status() {
        assertEquals(
            RoutineRunText(false, Res.string.robot_routine_not_started_status, listOf("Kitchen first", "offline")),
            routineRunText("Kitchen first", SceneRunResult(accepted = false, status = "offline")),
        )
    }
}
