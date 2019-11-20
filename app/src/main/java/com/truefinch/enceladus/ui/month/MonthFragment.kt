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
import kotlinx.android.synthetic.main.fragment_month.view.*
import java.time.LocalTime
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called upon change of the selected day
     *
     * @param widget the view associated with this listener
     * @param date   the month picked, as the first day of the month
     */
    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
        Log.d("DEBUG", "monthView.onMonthChanged")
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
    }

    /**
     * Called when a user long clicks on a day.
     * There is no logic to prevent multiple calls for the same date and state.
     *
     * @param widget the view associated with this listener
     * @param date   the date that was long clicked.
     */
    override fun onDateLongClick(widget: MaterialCalendarView, date: CalendarDay) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        viewModel = ViewModelProviders.of(this).get(MonthViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_month, container, false)

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.lastLoadedMonth.observe(this, Observer {
            val hashSet = decorator.eventsListToHashSet(it)
            decorator.setDates(hashSet)
            root.material_calendar_view.invalidateDecorators()
        })

        decorator = EventMonthDecorator(resources, 5f)

        root.material_calendar_view.setOnDateChangedListener(this)
        root.material_calendar_view.setOnDateLongClickListener(this)
        root.material_calendar_view.setOnMonthChangedListener(this)
        root.material_calendar_view.addDecorators(
            decorator,
            TodayMonthDecoratorView(
                resources.getDrawable(
                    R.drawable.ic_today_circle_background,
                    null
                )
            )
        )

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onMonthChanged(view.findViewById(R.id.material_calendar_view), CalendarDay.today())
    }
}
