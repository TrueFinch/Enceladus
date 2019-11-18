package com.truefinch.enceladus.repository.server.model

data class EventResponse(
    val count: Int,
    val data: List<EventServer>,
    val message: String,
    val offset: Long,
    val status: Int,
    val success: Boolean
)

data class EventInstanceResponse(
    val count: Int,
    val data: List<EventInstanceServer>,
    val message: String,
    val offset: Long,
    val status: Int,
    val success: Boolean
)

data class EventPatternResponse(
    val count: Int,
    var data: List<EventPatternServer>,
    val message: String,
    val offset: Long,
    val status: Int,
    val success: Boolean
)