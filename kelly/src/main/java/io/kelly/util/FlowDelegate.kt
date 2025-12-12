package io.kelly.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> SavedStateHandle.delegatedState(
    initialValue: T,
): ReadWriteProperty<Any?, T> {
    return object : ReadWriteProperty<Any?, T> {
        private var _state: MutableState<T>? = null
        private fun ensureState(property: KProperty<*>): MutableState<T> {
            if (_state == null) {
                val savedValue: T = this@delegatedState[ property.name] ?: initialValue
                _state = mutableStateOf(savedValue)
            }
            return _state!!
        }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return ensureState(property).value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            ensureState( property).value = value
            this@delegatedState[property.name] = value
        }
    }
}

fun <T> SavedStateHandle.state(
    initialValue: T,
): ReadOnlyProperty<Any?, MutableStateFlow<T>> {
    return object : ReadOnlyProperty<Any?, MutableStateFlow<T>> {
        private var _flow: MutableStateFlow<T>? = null
        override fun getValue(thisRef: Any?, property: KProperty<*>): MutableStateFlow<T> {
            if (_flow == null) {
                _flow = this@state.getMutableStateFlow(property.name, initialValue)
            }
            return _flow!!
        }
    }
}

fun <T> shared(
    replay: Int = 0,
    extraBufferCapacity: Int = 1,
    onBufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
): Lazy<MutableSharedFlow<T>> {
    return lazy {
        MutableSharedFlow(
            replay = replay,
            extraBufferCapacity = extraBufferCapacity,
            onBufferOverflow = onBufferOverflow
        )
    }
}

