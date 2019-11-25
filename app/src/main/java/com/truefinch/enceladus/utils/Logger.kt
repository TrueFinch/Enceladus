package com.truefinch.enceladus.utils

import android.util.Log
import com.truefinch.enceladus.utils.DateFormatterUtil.Companion.formatToPattern
import java.time.ZonedDateTime
import java.util.*

fun logD(threadID: Int, sender: String) {
    log("${getThreadLbl(threadID)} $sender")
}

fun LogD(threadID: Int, sender: String, message: String) {
    log("${getThreadLbl(threadID)} $sender $message")
}

fun logD(threadID: Int, sender: String, to: ZonedDateTime) {
    log("${getThreadLbl(threadID)} $sender to ${formatToPattern(dMYPattern, to)}")
}

fun logD(threadID: Int, sender: String, to: Calendar) {
    logD(threadID, sender, toZoneDateTime(to))
}

fun logD(threadID: Int, sender: String, from: ZonedDateTime, to: ZonedDateTime) {
    log(
        "${getThreadLbl(threadID)} $sender from ${formatToPattern(
            dMYPattern,
            from
        )} to ${formatToPattern(dMYPattern, to)}"
    )
}

fun logD(threadID: Int, sender: String, from: Calendar, to: Calendar) {
    logD(threadID, sender, toZoneDateTime(from), toZoneDateTime(to))
}

private fun log(message: String) {
    Log.d(debugTAG, message)
}

private fun getThreadLbl(threadID: Int): String {
    return "Thread $threadID:"
}

private var threadID: Int = 0
private val debugTAG: String = "DEBUG"
private val threadLbl: String = "Thread"
private val dMYPattern: String = "dd-MM-yyyy"
