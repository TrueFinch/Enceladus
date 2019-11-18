package com.truefinch.enceladus.repository.server.model

import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

data class EventRequest(
    val details: String?,
    val location: String?,
    val name: String?,
    val status: String?
) {
    constructor(entity: EventServer)
            : this(
        details = entity.details,
        location = entity.location,
        name = entity.name,
        status = entity.status
    )
}

data class PatternRequest(
    var duration: Duration,
    var ended_at: ZonedDateTime,
    var rrule: String?,
    var started_at: ZonedDateTime,
    var timezone: ZoneId
)

