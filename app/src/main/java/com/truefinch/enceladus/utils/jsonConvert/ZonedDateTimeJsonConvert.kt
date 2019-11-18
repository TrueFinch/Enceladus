package com.truefinch.enceladus.utils.jsonConvert

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime


class ZonedDateTimeJsonConvert : JsonSerializer<ZonedDateTime>, JsonDeserializer<ZonedDateTime> {

    override fun serialize(
        src: ZonedDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return src?.let { return JsonPrimitive(fromZonedDateTime(it)) }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZonedDateTime? {
        return json?.let { return toZonedDateTime(it.asLong) }
    }

    private fun toZonedDateTime(date: Long?): ZonedDateTime? {
        return date?.let {
            return fromLongUTC(date)
        }
    }

    private fun fromZonedDateTime(date: ZonedDateTime?): Long? {
        return date?.let {
            return toLongUTC(date)
        }
    }

    private fun fromLongUTC(l: Long): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.UTC)
    }

    private fun toLongUTC(z: ZonedDateTime): Long {
        val utc = z.withZoneSameInstant(ZoneOffset.UTC)
        return utc.toInstant().toEpochMilli()
    }
}