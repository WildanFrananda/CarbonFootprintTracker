package dev.frananda.carbonfootprinttracker.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    fun getCurrentDateInLocalTimezone(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date())
    }
}