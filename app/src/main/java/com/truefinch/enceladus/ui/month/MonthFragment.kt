package com.truefinch.enceladus.ui.month

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.Z
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.prolificinteractive.materialcalendarview.*
import com.truefinch.enceladus.R
import com.truefinch.enceladus.SharedViewModel
import com.truefinch.enceladus.ui.custom_views.EventMonthDecorator
import com.truefinch.enceladus.ui.custom_views.TodayMonthDecoratorView
import com.truefinch.enceladus.utils.DateFormatterUtil
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import com.truefinch.enceladus.utils.toYearMonth
import kotlinx.android.synthetic.main.fragment_month.*
import kotlinx.android.synthetic.main.fragment_month.view.*
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters
import java.util.*

class MonthFragment : Fragment(), OnDateSelectedListener, OnMonthChangedListener,
    OnDateLongClickListener {
    /**
     * Called when a user clicks on a day.
     * There is no logic to prevent multiple calls for the same date and state.
     *
     * @param widget   the view associated with this listener
     * @param date     the date that was selected or unselected
     * @param selected true if the day is now selected, false otherwise
     */
    override fun onDateSelected(
        widget: MaterialCalendarView,
        date: CalendarDay,
        selected: Boolean
    ) {
    }

    /**
     * Called upon change of the selected day
     *
     * @param widget the view associated with this listener
     * @param date   the month picked, as the first day of the month
     */
    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". monthView.onMonthChanged to ${date?.let {
                formatToPattern(
                    "MM-yyyy",
                    it
                )
            }}"
        )
        val month =
            date?.calendar?.toInstant()?.atZone(ZoneId.systemDefault()) ?: ZonedDateTime.now()
        val monthStart = month.toLocalDate()
            .with(TemporalAdjusters.firstDayOfMonth())
            .atStartOfDay(month.zone)
        val monthEnd = month.toLocalDate()
            .with(TemporalAdjusters.lastDayOfMonth())
            .atTime(LocalTime.MAX)
            .atZone(month.zone)

        sharedViewModel.fetchEvents(monthStart, monthEnd)
        val events = sharedViewModel.loadedMonths[YearMonth.of(
            month.year,
            month.monthValue
        )]

        if (events?.yearMonth != toYearMonth(
                date ?: material_calendar_view.currentDate
            ) || !events.loaded
        ) {
            return
        }
        val hashSet = decorator.eventsListToHashSet(events.list)
        decorator.setDates(hashSet)
        material_calendar_view.invalidateDecorators()
    }

    /**
     * Called when a user long clicks on a day.
     * There is no logic to prevent multiple calls for the same date and state.
     *
     * @param widget the view associated with this listener
     * @param date   the date that was long clicked.
     */
    override fun onDateLongClick(widget: MaterialCalendarView, date: CalendarDay) {
    }

    companion object {
        fun newInstance() = MonthFragment()
    }

    private lateinit var decorator: EventMonthDecorator
    private lateinit var viewModel: MonthViewModel
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". monthView.onCreateView"
        )
        viewModel = ViewModelProviders.of(this).get(MonthViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_month, container, false)

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        decorator = EventMonthDecorator(resources, 5f)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". monthView.onActivityCreated"
        )

        material_calendar_view.setOnDateChangedListener(this)
        material_calendar_view.setOnDateLongClickListener(this)
        material_calendar_view.setOnMonthChangedListener(this)
        material_calendar_view.addDecorators(
            decorator,
            TodayMonthDecoratorView(
                resources.getDrawable(
                    R.drawable.ic_today_circle_background,
                    null
                )
            )
        )

        sharedViewModel.lastLoadedMonth.observe(this, Observer {
            Log.d(
                "DEBUG",
                "Thread: " + Thread.currentThread().id.toString() + ". MonthView.onLoadingSuccess for ${formatToPattern(
                    "MM-yyyy",
                    it.yearMonth
                )}"
            )

//            if (!isInRange(material_calendar_view.currentDate, it.first))
//                return@Observer
            val hashSet = decorator.eventsListToHashSet(it.list)
            decorator.setDates(hashSet)
            material_calendar_view.invalidateDecorators()
        })
    }

    private fun isInRange(day: CalendarDay, range: YearMonth): Boolean {
        return day.isInRange(
            CalendarDay.from(range.year, range.monthValue, 1),
            CalendarDay.from(range.year, range.monthValue, range.atEndOfMonth().dayOfMonth)
        )
    }
}
