package com.truefinch.enceladus.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DateFormatterUtil {
    companion object {
        fun formatToPattern(pattern: String, zonedDateTime: ZonedDateTime): String {
            return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern))
        }

        fun formatToPattern(pattern: String, calendar: Calendar): String {
            return formatToPattern(pattern, toZoneDateTime(calendar))
        }

        fun formatToPattern(pattern: String, calendarDay: CalendarDay): String {
            return formatToPattern(pattern, toZoneDateTime(calendarDay))
        }

        fun formatToPattern(pattern: String, yearMonth: YearMonth): String {
            return yearMonth.format(DateTimeFormatter.ofPattern(pattern))
        }
    }
}