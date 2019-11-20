package com.truefinch.enceladus.ui.week

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.models.EventModel
import java.time.ZonedDateTime

class WeekViewModel : ViewModel() {
    private val server = EnceladusApp.instance.server
    private val api = EnceladusApp.instance.server.api
    private val repo = EnceladusApp.instance.repository

    private var _loadingSuccessCallback: OnLoadingSuccessCallback? = null

    interface OnLoadingSuccessCallback {
        fun onLoadingSuccess(events: List<EventModel>)
    }

    fun setOnLoadingSuccessListener(listener: (events: List<EventModel>) -> Unit) {
        _loadingSuccessCallback = object : OnLoadingSuccessCallback {
            override fun onLoadingSuccess(events: List<EventModel>) = listener(events)
        }
    }

}
