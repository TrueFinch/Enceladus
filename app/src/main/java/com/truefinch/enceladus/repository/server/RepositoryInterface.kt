package com.truefinch.enceladus.repository.server

import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.repository.server.model.EventInstanceResponse
import com.truefinch.enceladus.repository.server.model.EventRequest
import com.truefinch.enceladus.repository.server.model.PatternRequest
import io.reactivex.Completable
import io.reactivex.Single
import java.time.ZonedDateTime

interface RepositoryInterface {

    fun fromTo(startLocal: ZonedDateTime, endLocal: ZonedDateTime): Single<List<EventModel>>
//    fun insertEvent(eventRequest: EventRequest, patternRequests: ArrayList<PatternRequest>): Completable
}