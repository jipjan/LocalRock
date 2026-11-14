package com.kodraliu.localrock.ui.vacuum


expect fun formatCleanRecordTime(epochSeconds: Long): String

/** The time of day of an epoch second, short ("13:07"), in the phone's time zone. */
expect fun formatClockTime(epochSeconds: Long): String
