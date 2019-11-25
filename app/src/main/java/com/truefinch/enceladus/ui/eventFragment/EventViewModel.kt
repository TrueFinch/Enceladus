package com.truefinch.enceladus.ui.eventFragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truefinch.enceladus.EnceladusApp
import com.truefinch.enceladus.SharedViewModel
import com.truefinch.enceladus.models.EventModel
import com.truefinch.enceladus.utils.EventMode
import com.truefinch.enceladus.utils.LogD
import java.time.ZonedDateTime

class EventViewModel : ViewModel() {
    val server = EnceladusApp.instance.server
    val api = EnceladusApp.instance.server.api

    val startDateTime = MutableLiveData<ZonedDateTime>()
    val endDateTime = MutableLiveData<ZonedDateTime>()
    val eventTitle = MutableLiveData<String>("")
    val eventDescription = MutableLiveData<String>("")
    val eventLocation = MutableLiveData<String>("")
    var eventMode = MutableLiveData<EventMode>(EventMode.NONE)

    private var isUnsaved = MediatorLiveData<Boolean>()
    val loadedData = MutableLiveData<Boolean>(false)

    init {
        isUnsaved.apply {
            addSource(startDateTime) { isUnsaved.value = true }
            addSource(endDateTime) { isUnsaved.value = true }
            addSource(eventTitle) { isUnsaved.value = true }
            addSource(eventDescription) { isUnsaved.value = true }
            addSource(eventLocation) { isUnsaved.value = true }
            addSource(eventMode) { isUnsaved.value = true }
        }
    }

    fun setEventData(eventModel: EventModel, mode: EventMode = EventMode.SHOW) {
        assert(mode == EventMode.NONE) {
            LogD(
                Thread.currentThread().id.toInt(), "EventViewModel.setEventData",
                "FAILED ${eventModel.title ?: "No title"} in $mode mode"
            )
        }
        LogD(
            Thread.currentThread().id.toInt(), "EventViewModel.setEventData",
            "${eventModel.title ?: "No title"} in $mode mode"
        )

        eventModel.apply {
            eventTitle.value = title ?: ""
            eventDescription.value = description ?: ""
            eventLocation.value = location ?: ""
            startDateTime.value = local_start_date
            endDateTime.value = local_end_date
        }
        eventMode.value = mode
        loadedData.value = true
        //TODO: get pattern by id from server
    }

    fun sendEvent() {
//        api.createEvent(EventRequest(eventDescription, null, eventTitle, "mem"))
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe ({
//                Log.d("debug","Succees")
//            }, {
//                Log.d("debug", it.toString())
//            })
//        api.createPattern()
    }

    fun isUnsavedEventExist(): Boolean {
        return isUnsaved.value ?: false
    }

    fun clear() {
        startDateTime.value = null
        endDateTime.value = null
        eventTitle.value = null
        eventDescription.value = null
        isUnsaved.value = false
        loadedData.value = false
    }


}
