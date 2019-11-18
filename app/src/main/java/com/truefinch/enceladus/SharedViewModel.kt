package com.truefinch.enceladus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.ZonedDateTime

class SharedViewModel : ViewModel() {
    private val _selectedDateTime by lazy {
        return@lazy MutableLiveData<ZonedDateTime>(ZonedDateTime.now())
    }

    var selectedDateTime: ZonedDateTime
        get() = _selectedDateTime.value!!
        set(value) {
            _selectedDateTime.value = value
        }
}