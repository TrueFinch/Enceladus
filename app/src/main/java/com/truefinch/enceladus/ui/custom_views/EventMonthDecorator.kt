package com.truefinch.enceladus.ui.custom_views

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.truefinch.enceladus.R
import com.truefinch.enceladus.models.EventModel
import java.security.AccessControlContext
import java.time.Duration
import java.time.Period
import java.time.ZonedDateTime
import java.util.*
import kotlin.collections.HashSet

class EventMonthDecorator(
    private val resources: Resources,
    private var radius: Float = 0f
) : DayViewDecorator {

    init {
        val out: TypedValue = TypedValue()
        resources.getValue(R.dimen.eventMonthDotRadius, out, true)
        radius = out.float
    }

    fun setDates(dates: HashSet<ZonedDateTime>) {
        datesWithEvent.clear()
        dates.forEach {
            datesWithEvent.add(
                CalendarDay.from(it.year, it.monthValue - 1, it.dayOfMonth)
            )
        }
    }

    /**
     * Determine if a specific day should be decorated
     *
     * @param day [CalendarDay] to possibly decorate
     * @return true if this decorator should be applied to the provided day
     */
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return datesWithEvent.contains(day)
    }

    /**
     * Set decoration options onto a facade to be applied to all relevant days
     *
     * @param view View to decorate
     */
    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(DotSpan(radius, color))
    }

    public fun eventsListToHashSet(list: List<EventModel>): HashSet<ZonedDateTime> {
        val hash: HashSet<ZonedDateTime> = hashSetOf()
        list.forEach { em ->
            val period =
                Period.between(em.local_start_date.toLocalDate(), em.local_end_date.toLocalDate())
                    .days
            for (day in 0..period) {
                hash.add(em.local_start_date.plusDays(day.toLong()))
            }
        }
        return hash
    }

    private val datesWithEvent: HashSet<CalendarDay> = hashSetOf()
    private val color = resources.getColor(R.color.colorPrimaryLight, null)
}