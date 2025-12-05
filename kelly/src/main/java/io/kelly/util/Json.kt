package io.kelly.util

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getIntOrNull(key: String): Int? {
    if (isNull(key)) return null
    return try {
        getInt(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getStringOrNull(key: String): String? {
    if (isNull(key)) return null
    return try {
        getString(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getLongOrNull(key: String): Long? {
    if (isNull(key)) return null
    return try {
        getLong(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getDoubleOrNull(key: String): Double? {
    if (isNull(key)) return null
    return try {
        getDouble(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getBooleanOrNull(key: String): Boolean? {
    if (isNull(key)) return null
    return try {
        getBoolean(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getJSONObjectOrNull(key: String): JSONObject? {
    if (isNull(key)) return null
    return try {
        getJSONObject(key)
    } catch (_: Exception) {
        null
    }
}

fun JSONObject.getJSONArrayOrNull(key: String): JSONArray? {
    if (isNull(key)) return null
    return try {
        getJSONArray(key)
    } catch (_: Exception) {
        null
    }
}


fun String?.tryAsJSONObject(): JSONObject? {
    if (this.isNullOrBlank()) return null
    return try {
        JSONObject(this)
    } catch (_: Exception) {
        null
    }
}

fun String?.tryAsJSONArray(): JSONArray? {
    if (this.isNullOrBlank()) return null
    return try {
        JSONArray(this)
    } catch (_: Exception) {
        null
    }
}

