package com.truefinch.enceladus.repository.server.model

import java.time.ZonedDateTime

data class EventInstanceServer(
    val event_id: Long,
    val pattern_id: Long,

    val started_at: ZonedDateTime,
    val ended_at: ZonedDateTime
)
