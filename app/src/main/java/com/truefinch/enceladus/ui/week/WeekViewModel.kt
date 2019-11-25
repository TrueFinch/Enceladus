package com.truefinch.enceladus.ui.week

import androidx.lifecycle.ViewModel
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.SharedViewModel
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.EventMode
import com.truefinch.enceladus.utils.LogD

class WeekViewModel : ViewModel() {
    fun isUnsavedEventExist(): Boolean {
        return EnceladusApp.instance.sharedViewModel.isUnsavedEventExist()
    }

    fun provideEventData(event: EventModel, mode: EventMode) {
        LogD(
            Thread.currentThread().id.toInt(),
            "WeekViewModel.provideEventData",
            event.title ?: "No title"
        )
        viewOrEventClickListener?.onViewOrEventClick(event, mode)
    }

    private val server = EnceladusApp.instance.server
    private val api = EnceladusApp.instance.server.api
    private val repo = EnceladusApp.instance.repository

    var viewOrEventClickListener: OnViewOrEventClickListener? = null

    interface OnViewOrEventClickListener {
        fun onViewOrEventClick(event: EventModel, mode: EventMode)
    }

    fun setOnViewOrEventClickListener(listener: (event: EventModel, mode: EventMode) -> Unit) {
        viewOrEventClickListener = object : OnViewOrEventClickListener {
            override fun onViewOrEventClick(event: EventModel, mode: EventMode) =
                listener(event, mode)
        }
    }
}
