package com.truefinch.enceladus.repository.server

import com.truefinch.enceladus.repository.server.model.*
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

const val request = "/api/v1"

interface PlannerApi {
    // Event
    @GET("$request/events")
    fun getEventsOffset(
        @Query("count") count: Int = 100,
        @Query("offset") offset: Long = 0
//        @Query("owner_id") owner_id: String,
//        @Query("id") id: List<Long>,
//        @Query("from") from: Long,
//        @Query("to") to: Long,
//        @Query("created_from") created_from: Long,
//        @Query("created_to") created_to: Long,
//        @Query("updated_from") updated_from: Long,
//        @Query("updated_to") updated_to: Long
    ): Observable<EventResponse>

    // [from, to]
    @GET("$request/events")
    fun getEventsFromTo(
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Single<EventResponse>

    @POST("$request/events")
    fun createEvent(
        @Body event: EventRequest
    ): Single<EventResponse>

    @GET("$request/events/{id}")
    fun getEventById(@Path("id") id: Long): Single<EventResponse>

    @DELETE("$request/events/{id}")
    fun deleteEventById(@Path("id") id: Long): Single<EventResponse>

    @PATCH("$request/events/{id}")
    fun updateEvent(
        @Path("id") id: Long,
        @Body updates: EventRequest
    ): Single<EventResponse>

    // [from, to]
    @GET("$request/events/instances")
    fun getEventsInstancesFromTo(
        @Query("from") from: Long,
        @Query("to") to: Long
//        @Query("owner_id") owner_id: String,
//        @Query("count") count: Int = 100,
//        @Query("offset") offset: Long = 0
//        @Query("id") id: List<Long>,
//        @Query("created_from") created_from: Long,
//        @Query("created_to") created_to: Long,
//        @Query("updated_from") updated_from: Long,
//        @Query("updated_to") updated_to: Long
    ): Single<EventInstanceResponse>


    // Pattern
    @GET("$request/patterns")
    fun getPatterns(
        @Query("event_id") event_id: Long
    ): Single<EventPatternResponse>

    @GET("$request/patterns")
    fun getPatternsFromTo(
        @Query("from") from: Long,
        @Query("to") to: Long
    ): Single<EventPatternResponse>

    @POST("$request/patterns")
    fun createPattern(
        @Query("event_id") event_id: Long,
        @Body pattern: PatternRequest
    ): Single<EventPatternResponse>

    @GET("$request/patterns/{id}")
    fun getPatternById(@Path("id") id: Long): Single<EventPatternResponse>

    @DELETE("$request/patterns/{id}")
    fun deletePatternById(@Path("id") id: Long): Single<EventPatternResponse>

    @PATCH("$request/patterns/{id}")
    fun updatePattern(
        @Path("id") id: Long,
        @Body updates: PatternRequest
    ): Single<EventPatternResponse>

}