package com.truefinch.enceladus.models

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
data class EventModel internal constructor(
    val id: Long,
    val title: String,
    private val location: String,
    val pattern_id: Long,
    val isAllDay: Boolean,
    private val isCanceled: Boolean,
    var start_date: ZonedDateTime,
    var end_date: ZonedDateTime
) {

    init {
        start_date = start_date.withZoneSameInstant(ZoneId.systemDefault())
        end_date = end_date.withZoneSameInstant(ZoneId.systemDefault())
    }

    val local_start_date: ZonedDateTime
        get() = start_date.withZoneSameLocal(ZoneId.systemDefault())

    val local_finish_date: ZonedDateTime
        get() = end_date.withZoneSameLocal(ZoneId.systemDefault())
}