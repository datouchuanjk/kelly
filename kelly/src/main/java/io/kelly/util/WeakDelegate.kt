package io.kelly.util

import java.lang.ref.WeakReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class WeakDelegate<T : Any>(initialValue: T? = null) : ReadWriteProperty<Any?, T?> {
    private var weakReference = initialValue?.let { WeakReference(it) }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakReference?.get()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        weakReference = value?.let { WeakReference(it) }
    }
}