package com.truefinch.enceladus.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alamkanak.weekview.WeekViewDisplayable
import com.truefinch.enceladus.ui.events.WeekEvent
import java.time.LocalDate

class WeekViewModel : ViewModel() {

    val events = MutableLiveData<List<WeekViewDisplayable<WeekEvent>>>()

//    fun fetchEvents(startDate: LocalDate, endDate: LocalDate) {
//        events.value = database.getEventsInRange(startDate.toCalendar(), endDate.toCalendar())
//    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is week Fragment"
    }
    val text: LiveData<String> = _text
    // TODO: Implement the ViewModel
}
