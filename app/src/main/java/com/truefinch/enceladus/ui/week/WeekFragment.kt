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
import com.truefinch.enceladus.R
import com.truefinch.enceladus.SharedViewModel

import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.ui.events.WeekEventView
import com.truefinch.enceladus.utils.lazyView
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
        Log.d("DEBUG", "WeekView.onLoadMore")

        val month = startDate.toInstant().atZone(ZoneId.systemDefault())
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
     * Called when the month displayed in [WeekView] changes.
     * @param startDate A [Calendar] representing the start date of the month
     * @param endDate A [Calendar] representing the end date of the month
     * @return The list of [WeekViewDisplayable] of the provided month
     */
    override fun onMonthChange(
        startDate: Calendar,
        endDate: Calendar
    ): List<WeekViewDisplayable<WeekEventView>> {
        Log.d("DEBUG", "MonthChange")
        val month = startDate.toInstant().atZone(ZoneId.systemDefault())
        val pair = YearMonth.of(month.year, month.monthValue)
        val buf = sharedViewModel.loadedMonths[pair]
        return buf?.map {
            WeekEventView(it, resources.getColor(R.color.event))
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
        viewModel = ViewModelProviders.of(this).get(WeekViewModel::class.java)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.lastLoadedMonth.observe(this, Observer {
            onLoadingSuccess(it)
        })

//        viewModel.setOnLoadingSuccessListener {
//            onLoadingSuccess(it)
//        }

        val root = inflater.inflate(R.layout.fragment_week, container, false)
        val wv = root.findViewById<WeekView<WeekEventView>>(R.id.weekView)
        wv.onMonthChangeListener = this
        wv.scrollListener = this
        wv.scrollListener = this
        wv.onLoadMoreListener = this
        return root
    }

    @Synchronized
    private fun onLoadingSuccess(events: List<EventModel>) {
        if (events.isEmpty())
            return
        val a = events.map {
            WeekEventView(it, resources.getColor(R.color.event))
        }
        weekView.submit(a)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when an empty area of [WeekView] is clicked.
     *
     * @param time A [Calendar] with the date and time of the clicked position
     */
    override fun onEmptyViewClicked(time: Calendar) {
    }
}
