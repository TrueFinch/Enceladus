package com.truefinch.enceladus.ui.events

import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import com.alamkanak.weekview.WeekView
import com.truefinch.enceladus.R
import com.truefinch.enceladus.models.EventModel
import java.util.*

//this is wrapper for event to use it in week tab
class WeekEventView(
    var event: EventModel,
    val color: Int
) : WeekViewDisplayable<WeekEventView> {

    /**
     * Returns a [WeekViewEvent] for use in [WeekView].
     */
    override fun toWeekViewEvent(): WeekViewEvent<WeekEventView> {
        val style = WeekViewEvent.Style.Builder()
            .setBackgroundColor(color)
            .setTextStrikeThrough(false)
            .build()

        return WeekViewEvent.Builder(this)
            .setId(event.id)
            .setTitle(event.title ?: "")
            .setStartTime(GregorianCalendar.from(event.start_date))
            .setEndTime(GregorianCalendar.from(event.end_date))
            .setLocation(event.location ?: "")
            .setAllDay(false)
            .setStyle(style)
            .build()
    }
}