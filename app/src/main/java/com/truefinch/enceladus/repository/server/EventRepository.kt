package com.truefinch.enceladus.repository.server

import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.toLongUTC
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.ZoneOffset
import java.time.ZonedDateTime

class EventRepository(val api: PlannerApi) : RepositoryInterface {
    override fun fromTo(
        startLocal: ZonedDateTime,
        endLocal: ZonedDateTime
    ): Single<List<EventModel>> {
        val startUTC = toLongUTC(startLocal.withZoneSameInstant(ZoneOffset.UTC))
        val endUTC = toLongUTC(endLocal.withZoneSameInstant(ZoneOffset.UTC))

        return api.getEventsInstancesFromTo(startUTC, endUTC)
            .map {
                it.data
            }
            .flattenAsFlowable { it }
            .flatMap { eventInstanceServer ->
                api.getEventById(eventInstanceServer.event_id)
                    .map { event -> event.data.first() }
                    .map { eventServer ->
                        EventModel(
                            eventInstanceServer.event_id,
                            eventServer.name,
                            eventServer.location,
                            eventInstanceServer.pattern_id,
                            eventInstanceServer.started_at,
                            eventInstanceServer.ended_at
                        )
                    }
                    .toFlowable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}