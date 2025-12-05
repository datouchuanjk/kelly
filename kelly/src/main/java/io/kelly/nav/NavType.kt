package io.kelly.nav


import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
): NavType<T> {
    return object : NavType<T>(isNullableAllowed = isNullableAllowed) {
        override fun get(bundle: Bundle, key: String): T? {
            return bundle.getString(key)?.let {
                json.decodeFromString(it)
            }
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, json.encodeToString(value))
        }
    }
}