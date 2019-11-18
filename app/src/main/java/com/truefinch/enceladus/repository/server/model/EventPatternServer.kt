package com.truefinch.enceladus.repository.server.model

import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

data class EventPatternServer(
    val id: Long,

    val created_at: ZonedDateTime,
    val updated_at: ZonedDateTime,

    var started_at: ZonedDateTime,
    var duration: Duration,
    var ended_at: ZonedDateTime,
    var rrule: String?,
    var timezone: ZoneId
) {
}
