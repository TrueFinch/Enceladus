package com.truefinch.enceladus

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truefinch.enceladus.models.EventModel
import java.time.YearMonth
import java.time.ZonedDateTime

class SharedViewModel : ViewModel() {
    var selectedDateTime: ZonedDateTime
        get() = _selectedDateTime.value!!
        set(value) {
            _selectedDateTime.value = value
        }

    val lastLoadedMonth
        get() = _loadedMonthObserver

    val loadedMonths: HashMap<YearMonth, List<EventModel>>
        get() {
            val loadedMonths: HashMap<YearMonth, List<EventModel>> = hashMapOf()
            _loadedMonths.forEach { (t, u) ->
                loadedMonths[t] = u.value ?: emptyList()
            }
            return loadedMonths
        }

    //month year
    private val _loadedMonths: HashMap<YearMonth, MutableLiveData<List<EventModel>>> by lazy {
        HashMap<YearMonth, MutableLiveData<List<EventModel>>>()
    }

    private val _loadedMonthObserver: MediatorLiveData<List<EventModel>> by lazy {
        MediatorLiveData<List<EventModel>>()
    }

    private val _selectedDateTime by lazy {
        return@lazy MutableLiveData<ZonedDateTime>(ZonedDateTime.now())
    }

    private fun isLoaded(yearMonth: YearMonth): Boolean {
        return _loadedMonths.contains(yearMonth)
    }

    fun fetchEvents(monthStart: ZonedDateTime, monthEnd: ZonedDateTime) {
        val pair = YearMonth.of(monthStart.year, monthStart.monthValue)
        if (!isLoaded(pair)) {
            val events = MutableLiveData<List<EventModel>>(emptyList())
            _loadedMonths[pair] = events
            _loadedMonthObserver.addSource(events) {
                _loadedMonthObserver.value = it
            }
            loadEvents(monthStart, monthEnd)
        }
    }

    private fun loadEvents(monthStart: ZonedDateTime, monthEnd: ZonedDateTime) {
        val pair = YearMonth.of(monthStart.year, monthStart.monthValue)
        val subscription = repo.fromTo(monthStart, monthEnd)
            .subscribe(
                {
                    _loadedMonths[pair]?.value = it
//                    _loadingSuccessCallback?.onLoadingSuccess(it)
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