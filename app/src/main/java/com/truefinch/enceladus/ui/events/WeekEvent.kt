package com.truefinch.enceladus.ui.events

import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import com.alamkanak.weekview.WeekView
import com.truefinch.enceladus.models.EventModel
import java.util.*

//this is wrapper for event to use it in week tab
class WeekEvent(
    var event: EventModel,
    val isFake: Boolean,
    val color: Int
) : WeekViewDisplayable<WeekEvent> {

    /**
     * Returns a [WeekViewEvent] for use in [WeekView].
     */
    override fun toWeekViewEvent(): WeekViewEvent<WeekEvent> {
        return WeekViewEvent.Builder<WeekEvent>(this)
            .setId(event.id)
            .setTitle(event.title)
            .setStartTime(GregorianCalendar.from(event.start_date))
            .setEndTime(GregorianCalendar.from(event.end_date))
            .build()
    }
}