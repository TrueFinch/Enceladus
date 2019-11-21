package com.truefinch.enceladus.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.*
import java.util.*

fun toLongUTC(z: ZonedDateTime): Long {
    val utc = z.withZoneSameLocal(ZoneOffset.UTC)
    return utc.toInstant().toEpochMilli()
}

fun fromLongUTC(l: Long): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.UTC)
}

fun toCalendar(zonedDateTime: ZonedDateTime): Calendar {
    return GregorianCalendar.from(zonedDateTime)
}

fun toCalendar(calendarDay: CalendarDay): Calendar {
    return calendarDay.calendar
}

fun toCalendarDay(zonedDateTime: ZonedDateTime): CalendarDay {
    return CalendarDay()
}

fun toZoneDateTime(calendar: Calendar): ZonedDateTime {
    return calendar.toInstant().atZone(ZoneId.systemDefault())
}

fun toZoneDateTime(calendarDay: CalendarDay): ZonedDateTime {
    return toZoneDateTime(calendarDay.calendar)
}

fun toYearMonth(zonedDateTime: ZonedDateTime): YearMonth {
    return YearMonth.from(zonedDateTime)
}

fun toYearMonth(calendar: Calendar): YearMonth {
    return YearMonth.from(toZoneDateTime(calendar))
}

fun toYearMonth(calendarDay: CalendarDay): YearMonth {
    return YearMonth.from(toZoneDateTime(calendarDay.calendar))
}