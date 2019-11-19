package com.truefinch.enceladus.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

fun toLongUTC(z: ZonedDateTime): Long {
    val utc = z.withZoneSameInstant(ZoneOffset.UTC)
    return utc.toInstant().toEpochMilli()
}

fun fromLongUTC(l: Long): ZonedDateTime {
    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.UTC)
}