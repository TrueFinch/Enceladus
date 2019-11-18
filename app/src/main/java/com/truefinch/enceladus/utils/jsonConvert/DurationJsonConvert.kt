package com.truefinch.enceladus.utils.jsonConvert

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Duration


class DurationJsonConvert : JsonSerializer<Duration>, JsonDeserializer<Duration> {

    override fun serialize(
        src: Duration?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return src?.let { return JsonPrimitive(fromDuration(src)) }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Duration? {
        return json?.let { return toDuration(it.asLong) }
    }

    private fun toDuration(l: Long?): Duration? {
        if (l == null) {
            return null
        }
        return Duration.ofMillis(l)
    }

    private fun fromDuration(d: Duration?): Long? {
        return d?.toMillis()
    }
}