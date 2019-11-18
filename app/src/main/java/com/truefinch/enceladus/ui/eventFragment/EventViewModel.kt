package com.truefinch.enceladus.ui.eventFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.ZonedDateTime

class EventViewModel : ViewModel() {
    var startDateTime: ZonedDateTime
        get() = _startDateTime.value!!
        set(value) {
            _startDateTime.value = value
        }

    var endDateTime: ZonedDateTime
        get() = _endDateTime.value!!
        set(value) {
            _endDateTime.value = value
        }

    var eventTitle: String
        get() = _eventTitle.value!!
        set(value) {
            _eventTitle.value = value
        }

    var eventDescription: String
        get() = _eventDescription.value!!
        set(value) {
            _eventDescription.value = value
        }

    private val _startDateTime by lazy {
        return@lazy MutableLiveData<ZonedDateTime>()
    }

    private val _endDateTime by lazy {
        return@lazy MutableLiveData<ZonedDateTime>()
    }

    private val _eventTitle by lazy {
        return@lazy MutableLiveData<String>("")
    }

    private val _eventDescription by lazy {
        return@lazy MutableLiveData<String>("")
    }

    public fun clear() {
        _startDateTime.value = null
        _endDateTime.value = null
        _eventTitle.value = null
        _eventDescription.value = null
    }
}
