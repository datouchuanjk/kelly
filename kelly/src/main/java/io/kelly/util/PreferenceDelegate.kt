package io.kelly.util

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun prefBool(default: Boolean = false): ReadWriteProperty<Any?, Boolean> {
    return PreferenceDelegate(
        default,
        SharedPreferences::getBoolean,
        SharedPreferences.Editor::putBoolean
    )
}

fun prefString(default: String = ""): ReadWriteProperty<Any?, String> {
    return PreferenceDelegate(
        default,
        SharedPreferences::getString,
        SharedPreferences.Editor::putString
    )
}


fun prefInt(default: Int = 0): ReadWriteProperty<Any?, Int> {
    return PreferenceDelegate(
        default,
        SharedPreferences::getInt,
        SharedPreferences.Editor::putInt
    )
}


fun prefLong(default: Long = 0L): ReadWriteProperty<Any?, Long> {
    return PreferenceDelegate(
        default,
        SharedPreferences::getLong,
        SharedPreferences.Editor::putLong
    )
}

internal class PreferenceDelegate<T>(
    private val defaultValue: T,
    private val getter: SharedPreferences.(String, T) -> T?,
    private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return ContextManager.sharedPreferences.getter(property.name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        ContextManager.sharedPreferences.edit {
            setter(property.name, value)
        }
    }
}