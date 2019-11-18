package com.truefinch.enceladus.utils.jsonConvert

import com.google.gson.*
import java.lang.reflect.Type
import java.time.ZoneId


class ZoneIdJsonConvert : JsonSerializer<ZoneId>, JsonDeserializer<ZoneId> {

    override fun serialize(
        src: ZoneId?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return src?.let { return JsonPrimitive(fromZoneId(it)) }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ZoneId? {
        return json?.let { return toZoneId(it.asString) }
    }

    private fun toZoneId(l: String?): ZoneId? {
        if (l == null) {
            return null
        }
        return ZoneId.of(l)
    }

    private fun fromZoneId(d: ZoneId?): String? {
        return d?.toString()
    }
}