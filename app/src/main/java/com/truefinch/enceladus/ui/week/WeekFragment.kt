package com.truefinch.enceladus.ui.week

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alamkanak.weekview.*
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.R
import com.truefinch.enceladus.SharedViewModel

import com.truefinch.enceladus.ui.events.WeekEventView
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import com.truefinch.enceladus.utils.lazyView
import com.truefinch.enceladus.utils.toYearMonth
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.*


class WeekFragment : Fragment(), OnMonthChangeListener<WeekEventView>, OnLoadMoreListener,
    ScrollListener, OnEmptyViewLongClickListener, OnEmptyViewClickListener {
    companion object {
        fun newInstance() = WeekFragment()
    }

    /**
     * Called when the month displayed in [WeekView] changes.
     * @param startDate A [Calendar] representing the start date of the month
     * @param endDate A [Calendar] representing the end date of the month
     */
    override fun onLoadMore(startDate: Calendar, endDate: Calendar) {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onLoadMore from ${formatToPattern(
                "dd-MM-yyyy",
                startDate
            )} " +
                    "to ${formatToPattern("dd-MM-yyyy", endDate)}"
        )

        val month = startDate.toInstant().atZone(ZoneId.systemDefault())
        val monthStart = month.toLocalDate()
            .with(TemporalAdjusters.firstDayOfMonth())
            .atStartOfDay(month.zone)
        val monthEnd = month.toLocalDate()
            .with(TemporalAdjusters.lastDayOfMonth())
            .atTime(LocalTime.MAX)
            .atZone(month.zone)
        if (sharedViewModel.isLoaded(toYearMonth(month))) {
            sharedViewModel.loadedMonths[toYearMonth(month)]?.list?.map {
                WeekEventView(it, resources.getColor(R.color.event, null))
            }?.let { /*weekView.submit(it) */ }
            return
        }
        sharedViewModel.fetchEvents(monthStart, monthEnd)
    }

    /**
     * Called when the month displayed in [WeekView] changes.
     * @param startDate A [Calendar] representing the start date of the month
     * @param endDate A [Calendar] representing the end date of the month
     * @return The list of [WeekViewDisplayable] of the provided month
     */
    override fun onMonthChange(
        startDate: Calendar,
        endDate: Calendar
    ): List<WeekViewDisplayable<WeekEventView>> {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() +
                    ". WeekView.onMonthChange from ${formatToPattern("dd-MM-yyyy", startDate)}" +
                    "to ${formatToPattern("dd-MM-yyyy", startDate)}"
        )
        val month = startDate.toInstant().atZone(ZoneId.systemDefault())
        val pair = YearMonth.of(month.year, month.monthValue)
        val buf = sharedViewModel.loadedMonths[pair]
        return buf?.list?.map {
            WeekEventView(it, resources.getColor(R.color.event, null))
        } ?: emptyList()
    }


    private lateinit var viewModel: WeekViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val weekView: WeekView<WeekEventView> by lazyView(R.id.weekView)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onCreateView"
        )
        viewModel = ViewModelProviders.of(this).get(WeekViewModel::class.java)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val root = inflater.inflate(R.layout.fragment_week, container, false)
        return root
    }

    //    @Synchronized
    private fun onLoadingSuccess(events: SharedViewModel.YearMonthEvents) {
        if (events.list.isEmpty() /*|| EnceladusApp.instance.tabManager.currentTabId != R.id.navigation_week*/)
            return
        val a = events.list.map {
            WeekEventView(it, resources.getColor(R.color.event, null))
        }
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onLoadingSuccess for ${formatToPattern(
                "MM-yyyy",
                events.yearMonth
            )}"
        )
//        Thread.sleep(2_000)
//        weekView.submit(a)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onActivityCreated"
        )

        weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretTime(hour: Int): String {
                return if (hour > 11) (hour - 12).toString() + " PM" else if (hour == 0) "12 M" else "$hour AM"
            }


            @SuppressLint("DefaultLocale")
            override fun interpretDate(date: Calendar): String {
                val weekdayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
                val weekday = weekdayNameFormat.format(date.time)
                val format = SimpleDateFormat("dd/MM", Locale.getDefault())

                return "%s\n%s".format(weekday.toUpperCase(), format.format(date.time))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onViewCreated"
        )

        sharedViewModel.lastLoadedMonth.observe(this, Observer {
            //            if (weekView.firstVisibleDate == null || !isInRange(weekView.firstVisibleDate!!, it.first))
            onLoadingSuccess(it)
        })

        weekView.scrollListener = this
        weekView.scrollListener = this
        weekView.onLoadMoreListener = this
        weekView.onMonthChangeListener = this
    }

    /**
     * Called when the first visible date has changed.
     *
     * @param date The new first visible date
     */
    override fun onFirstVisibleDateChanged(date: Calendar) {
        weekView.notifyDataSetChanged()
    }

    /**
     * Called when an empty area of [WeekView] is long-clicked.
     *
     * @param time A [Calendar] with the date and time of the clicked position
     */
    override fun onEmptyViewLongClick(time: Calendar) {
    }

    /**
     * Called when an empty area of [WeekView] is clicked.
     *
     * @param time A [Calendar] with the date and time of the clicked position
     */
    override fun onEmptyViewClicked(time: Calendar) {
    }

    private fun isInRange(calendar: Calendar, yearMonth: YearMonth): Boolean {
        return calendar.get(Calendar.MONTH) == yearMonth.monthValue
    }
}
