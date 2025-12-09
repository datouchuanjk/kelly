package io.kelly.util

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

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

fun <T> event(
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

