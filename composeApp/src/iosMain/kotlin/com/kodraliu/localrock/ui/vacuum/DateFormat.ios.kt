package com.kodraliu.localrock.ui.vacuum

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.dateWithTimeIntervalSince1970

actual fun formatCleanRecordTime(epochSeconds: Long): String {
    if (epochSeconds <= 0L) return "Unknown"
    val date = NSDate.dateWithTimeIntervalSince1970(epochSeconds.toDouble())

    val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        timeStyle = NSDateFormatterShortStyle
        doesRelativeDateFormatting = true
    }
    return formatter.stringFromDate(date)
}

actual fun formatClockTime(epochSeconds: Long): String {
    val formatter = NSDateFormatter().apply {
        dateStyle = platform.Foundation.NSDateFormatterNoStyle
        timeStyle = NSDateFormatterShortStyle
    }
    return formatter.stringFromDate(NSDate.dateWithTimeIntervalSince1970(epochSeconds.toDouble()))
}
