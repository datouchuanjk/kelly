package io.kelly.util


import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@PublishedApi
internal val appJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = false
}

inline fun <reified T> T.encodeJson(): String {
    return appJson.encodeToString(this)
}

inline fun <reified T> String.decodeJson(): T {
    return appJson.decodeFromString(this)
}