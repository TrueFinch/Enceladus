package com.truefinch.enceladus.ui.week

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alamkanak.weekview.*
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.R
import com.truefinch.enceladus.SharedViewModel
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.ui.custom_views.WeekEventView
import com.truefinch.enceladus.utils.*
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.util.*


class WeekFragment : Fragment(), OnMonthChangeListener<WeekEventView>, OnLoadMoreListener,
    ScrollListener, OnEmptyViewLongClickListener, OnEmptyViewClickListener,
    OnEventLongClickListener<WeekEventView>, OnEventClickListener<WeekEventView> {
    companion object {
        fun newInstance() = WeekFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        logD(Thread.currentThread().id.toInt(), "WeekFragment.onCreateView")

        viewModel = activity?.run {
            ViewModelProviders.of(this)[WeekViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        return inflater.inflate(R.layout.fragment_week, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD(Thread.currentThread().id.toInt(), "WeekFragment.onViewCreated")

        sharedViewModel.lastLoadedMonth.observe(this, Observer {
            onLoadingSuccess(it)
        })

        weekView.onEmptyViewLongClickListener = this
        weekView.onEmptyViewClickListener = this
        weekView.scrollListener = this
        weekView.onLoadMoreListener = this
        weekView.onMonthChangeListener = this
        weekView.onEventClickListener = this
        weekView.onEventLongClickListener = this
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        logD(Thread.currentThread().id.toInt(), "WeekFragment.onLoadMore")

        weekView.dateTimeInterpreter = object : DateTimeInterpreter {
            override fun interpretTime(hour: Int): String = when {
                hour > 11 -> "${(hour - 12)} PM"
                hour == 0 -> "12 M"
                else -> "$hour AM"
            }

            override fun interpretDate(date: Calendar): String =
                "${formatToPattern("EEE", date)}\n${formatToPattern("dd/MM", date)}"
        }
    }

    /**
     * Called when the month displayed in [WeekView] changes.
     * @param startDate A [Calendar] representing the start date of the month
     * @param endDate A [Calendar] representing the end date of the month
     * @return The list of [WeekViewDisplayable] of the provided month
     */
    override fun onMonthChange(
        startDate: Calendar, endDate: Calendar
    ): List<WeekViewDisplayable<WeekEventView>> {
        logD(Thread.currentThread().id.toInt(), "WeekFragment.onMonthChange", startDate, endDate)

        val buf = sharedViewModel.loadedMonths[toYearMonth(startDate)]
        return buf?.list?.map {
            WeekEventView(
                it,
                resources.getColor(R.color.event, null)
            )
        } ?: emptyList()
    }

    /**
     * Called when the month displayed in [WeekView] changes.
     * @param startDate A [Calendar] representing the start date of the month
     * @param endDate A [Calendar] representing the end date of the month
     */
    override fun onLoadMore(startDate: Calendar, endDate: Calendar) {
        logD(Thread.currentThread().id.toInt(), "WeekFragment.onLoadMore", startDate, endDate)

        val month = toZoneDateTime(startDate)
        val monthStart = month.toLocalDate()
            .with(TemporalAdjusters.firstDayOfMonth())
            .atStartOfDay(month.zone)
        val monthEnd = month.toLocalDate()
            .with(TemporalAdjusters.lastDayOfMonth())
            .atTime(LocalTime.MAX)
            .atZone(month.zone)
        if (sharedViewModel.isLoaded(toYearMonth(month))) {
            return
        }
        sharedViewModel.fetchEvents(monthStart, monthEnd)
    }

    /**
     * Called when an empty area of [WeekView] is long-clicked.
     *
     * @param time A [Calendar] with the date and time of the clicked position
     */
    override fun onEmptyViewLongClick(time: Calendar) {
        viewOrEventClickHandler {
            viewModel.provideEventData(EventModel.emptyEvent(time), EventMode.CREATE)
            EnceladusApp.instance.tabManager.switchTab(R.id.navigation_event)
        }
    }

    /**
     * Called when an empty area of [WeekView] is clicked.
     *
     * @param time A [Calendar] with the date and time of the clicked position
     */
    override fun onEmptyViewClicked(time: Calendar) {
        //TODO: do smth with that
    }

    /**
     * Called when an [EventChip] is long-clicked.
     *
     * @param data The [T] object associated with the [EventChip]'s [WeekViewEvent]
     * @param eventRect The [RectF] of the [EventChip]
     */
    override fun onEventLongClick(data: WeekEventView, eventRect: RectF) {
        LogD(
            Thread.currentThread().id.toInt(),
            "WeekFragment.onEventLongClick",
            data.event.title ?: "No title"
        )
        viewOrEventClickHandler {
            viewModel.provideEventData(data.event, EventMode.EDIT)
            EnceladusApp.instance.tabManager.switchTab(R.id.navigation_event)
        }
    }

    /**
     * Called when an [EventChip] is clicked.
     *
     * @param data The [T] object associated with the [EventChip]'s [WeekViewEvent]
     * @param eventRect The [RectF] of the [EventChip]
     */
    override fun onEventClick(data: WeekEventView, eventRect: RectF) {
        LogD(
            Thread.currentThread().id.toInt(),
            "WeekFragment.onEventClick",
            data.event.title ?: "No title"
        )
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Called when the first visible date has changed.
     *
     * @param date The new first visible date
     */
    override fun onFirstVisibleDateChanged(date: Calendar) {
        weekView.notifyDataSetChanged()
    }

    private fun onLoadingSuccess(events: SharedViewModel.YearMonthEvents) {
        //TODO: replace with custom logger
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + ". WeekView.onLoadingSuccess for ${formatToPattern(
                "MM-yyyy",
                events.yearMonth
            )}"
        )
        weekView.notifyDataSetChanged()
    }

    private fun viewOrEventClickHandler(callback: () -> Unit) {
        if (viewModel.isUnsavedEventExist()) {
            showConfirmDialog(
                DialogInterface.OnClickListener { _, _ -> callback() },
                DialogInterface.OnClickListener { _, _ -> }
            )
        } else {
            callback()
        }
    }

    private fun showConfirmDialog(
        positiveButtonClickListener: DialogInterface.OnClickListener,
        negativeButtonClickListener: DialogInterface.OnClickListener
    ): AlertDialog {
        return AlertDialog.Builder(this.context)
            //TODO: move string to resources
            .setTitle("Unsaved data")
            .setMessage("Do you really want to drop unsaved event data?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, positiveButtonClickListener)
            .setNegativeButton(android.R.string.no, negativeButtonClickListener)
            .show()
    }

    private lateinit var viewModel: WeekViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val weekView: WeekView<WeekEventView> by lazyView(R.id.weekView)
}
