package com.kodraliu.localrock.shared.model

import kotlinx.serialization.Serializable


@Serializable
data class FirmwareOtaResponse(
    val code: Int = 0,
    val msg: String = "",
    val result: FirmwareUpdateInfo? = null,
)

@Serializable
data class FirmwareUpdateInfo(
    /** Latest available firmware version. */
    val version: String? = null,
    /** Version currently installed on the device (per the cloud). */
    val currentVersion: String? = null,
    /** True when [version] is newer than [currentVersion] and an update can be applied. */
    val updatable: Boolean = false,
    /** Release notes / changelog. */
    val desc: String? = null,
    val releaseTime: String? = null,
    /** Mandatory update the device will apply regardless of user action. */
    val forceUpdate: Boolean = false,
)

/**
 * True when the answer names a firmware version, so it comes from something that checked.
 * local_roborock_server has no update route and answers this one with its generic
 * `{"ok": true, "route": ...}`; read as [FirmwareUpdateInfo] that has every field at its default,
 * which used to show as "Up to date" although nothing was checked.
 */
val FirmwareUpdateInfo.namesAVersion: Boolean get() = version != null || currentVersion != null
