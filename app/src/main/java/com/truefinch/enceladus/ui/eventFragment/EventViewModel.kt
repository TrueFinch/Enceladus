package com.truefinch.enceladus.ui.eventFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.ZonedDateTime

class EventViewModel : ViewModel() {
    private val _selectedDateTime = MutableLiveData<ZonedDateTime>().apply {
        value = ZonedDateTime.now()
    }
    val selectedDateTime: LiveData<ZonedDateTime> = _selectedDateTime
}
