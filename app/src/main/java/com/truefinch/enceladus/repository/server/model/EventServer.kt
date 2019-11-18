package com.truefinch.enceladus.repository.server.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime

data class EventServer(
    val id: Long,
    val owner_id: String,

    val created_at: ZonedDateTime,
    val updated_at: ZonedDateTime,

    var name: String?,
    var details: String?,
    var status: String?,
    var location: String?
) {

    init {
        if (name == null) {
            name = ""
        }
        if (status == null) {
            status = ""
        }
        if (location == null) {
            location = ""
        }
        if (details == null) {
            details = ""
        }
    }
}


