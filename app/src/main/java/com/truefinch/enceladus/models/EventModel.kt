package com.truefinch.enceladus.models

import android.os.Parcelable
import android.util.EventLogTags
import androidx.room.Entity
import com.truefinch.enceladus.ui.eventFragment.EventViewModel
import com.truefinch.enceladus.utils.toZoneDateTime
import kotlinx.android.parcel.Parcelize
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Entity
data class EventModel internal constructor(
    val id: Long,
    val title: String?,
    val location: String?,
    val description: String?,
    val pattern_id: Long,
    var start_date: ZonedDateTime,
    var end_date: ZonedDateTime
) {
    companion object {
        fun emptyEvent(): EventModel =
            emptyEvent(ZonedDateTime.now())

        fun emptyEvent(calendar: Calendar) =
            emptyEvent(toZoneDateTime(calendar))

        fun emptyEvent(zonedDateTime: ZonedDateTime): EventModel =
            EventModel(-1, "", "", "", -1, zonedDateTime, zonedDateTime.plusHours(1))
    }

    init {
        start_date = start_date.withZoneSameInstant(ZoneId.systemDefault())
        end_date = end_date.withZoneSameInstant(ZoneId.systemDefault())
    }

    val local_start_date: ZonedDateTime
        get() = start_date.withZoneSameLocal(ZoneId.systemDefault())

    val local_end_date: ZonedDateTime
        get() = end_date.withZoneSameLocal(ZoneId.systemDefault())
}