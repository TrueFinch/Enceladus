package com.truefinch.enceladus.ui.custom_views

import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayMonthDecoratorView(
    private val bgDrawable: Drawable
) : DayViewDecorator {
    private val today = CalendarDay.today()

    /**
     * Determine if a specific day should be decorated
     *
     * @param day [CalendarDay] to possibly decorate
     * @return true if this decorator should be applied to the provided day
     */
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return today == day
    }

    /**
     * Set decoration options onto a facade to be applied to all relevant days
     *
     * @param view View to decorate
     */
    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(bgDrawable)
    }
}