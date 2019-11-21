package com.truefinch.enceladus

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import java.time.YearMonth
import java.time.ZonedDateTime

class SharedViewModel : ViewModel() {

    class YearMonthEvents(
        var yearMonth: YearMonth,
        var loaded: Boolean,
        var list: List<EventModel>
    )

    var selectedDateTime: ZonedDateTime
        get() = _selectedDateTime.value!!
        set(value) {
            _selectedDateTime.value = value
        }

    val lastLoadedMonth
        get() = _loadedMonthObserver


    val loadedMonths: HashMap<YearMonth, YearMonthEvents>
        get() {
            val loadedMonths: HashMap<YearMonth, YearMonthEvents> = hashMapOf()
            _loadedMonths.forEach { (t, u) ->
                loadedMonths[t] = u.value ?: YearMonthEvents(t, false, emptyList())
            }
            return loadedMonths
        }

    @Volatile
    var _loadedMonths: HashMap<YearMonth, MutableLiveData<YearMonthEvents>> =
        hashMapOf()

    private val _loadedMonthObserver: MediatorLiveData<YearMonthEvents> by lazy {
        MediatorLiveData<YearMonthEvents>()
    }

    private val _selectedDateTime by lazy {
        return@lazy MutableLiveData<ZonedDateTime>(ZonedDateTime.now())
    }

    fun isLoaded(yearMonth: YearMonth): Boolean {
        return _loadedMonths.contains(yearMonth) && _loadedMonths[yearMonth]?.value?.loaded ?: false
    }

    private fun isAddToLoad(yearMonth: YearMonth): Boolean {
        return _loadedMonths.contains(yearMonth)
    }

    @Synchronized
    fun fetchEvents(monthStart: ZonedDateTime, monthEnd: ZonedDateTime) {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + " sharedViewModel.fetchEvents from " +
                    "${formatToPattern("dd-MM-yyyy", monthStart)} " +
                    "to ${formatToPattern("dd-MM-yyyy", monthEnd)}"
        )
        val pair = YearMonth.of(monthStart.year, monthStart.monthValue)
        if (!isAddToLoad(pair)) {
            val events = MutableLiveData<YearMonthEvents>(YearMonthEvents(pair, false, emptyList()))
            _loadedMonths[pair] = events
            _loadedMonthObserver.addSource(events) {
                if (!it.loaded || it.list.isEmpty())
                    return@addSource
                _loadedMonthObserver.value = it
            }
            loadEvents(monthStart, monthEnd)
        }
    }

    @Synchronized
    private fun loadEvents(monthStart: ZonedDateTime, monthEnd: ZonedDateTime) {
        Log.d(
            "DEBUG",
            "Thread: " + Thread.currentThread().id.toString() + " sharedViewModel.loadEvents from " +
                    "${formatToPattern("dd-MM-yyyy", monthStart)} " +
                    "to ${formatToPattern("dd-MM-yyyy", monthEnd)}"
        )
        val pair = YearMonth.of(monthStart.year, monthStart.monthValue)
        val subscription = repo.fromTo(monthStart, monthEnd)
            .subscribe(
                {
                    Log.d(
                        "DEBUG",
                        "Thread: " + Thread.currentThread().id.toString() + " sharedViewModel.loadEvents.requestSubscribe from " +
                                "${formatToPattern("dd-MM-yyyy", monthStart)} " +
                                "to ${formatToPattern("dd-MM-yyyy", monthEnd)}"
                    )
                    _loadedMonths[pair]?.value = YearMonthEvents(
                        yearMonth = pair,
                        loaded = true,
                        list = it.filter { model ->
                            !model.local_end_date.isBefore(monthStart)
                                    && !model.local_end_date.isAfter(monthEnd)
                        }
                    )
                },
                { error ->
                    Log.d("DEBUG", "loading error: " + error.message)
                })
    }

    // to work with server
    private val server = EnceladusApp.instance.server
    private val api = EnceladusApp.instance.server.api
    private val repo = EnceladusApp.instance.repository

    //TODO: remove or not remove? That's the question
//    private var _loadingSuccessCallback: OnLoadingSuccessCallback? = null
//
//    interface OnLoadingSuccessCallback {
//        fun onLoadingSuccess(events: List<EventModel>)
//    }
//
//    fun setOnLoadingSuccessListener(listener: (events: List<EventModel>) -> Unit) {
//        _loadingSuccessCallback = object : OnLoadingSuccessCallback {
//            override fun onLoadingSuccess(events: List<EventModel>) = listener(events)
//        }
//    }
}