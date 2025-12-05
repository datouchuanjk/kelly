package io.kelly.util

import androidx.compose.runtime.snapshots.Snapshot


fun <T> List<T>.padToMultiple(multiple: Int, fillElement: T): List<T> {
    require(multiple > 0) { "multiple must be positive" }
    val currentSize = this.size
    val remainder = currentSize % multiple
    if (remainder == 0) return this
    val paddingCount = multiple - remainder
    return ArrayList<T>(currentSize + paddingCount).apply {
        addAll(this@padToMultiple)
        repeat(paddingCount) {
            add(fillElement)
        }
    }
}


fun <T> List<T>.padToMultiple(multiple: Int): List<T?> {
    require(multiple > 0) { "multiple must be positive" }
    val currentSize = this.size
    val remainder = currentSize % multiple
    if (remainder == 0) return this
    val paddingCount = multiple - remainder
    return ArrayList<T?>(currentSize + paddingCount).apply {
        addAll(this@padToMultiple)
        repeat(paddingCount) {
            add(null)
        }
    }
}

fun <T> List<T>.findIndex(predicate: (T) -> Boolean): Int? {
    forEachIndexed { index, item ->
        if (predicate(item)) {
            return index
        }
    }
    return null
}

fun <T, R> MutableList<T>.withMutableSnapshot(action: MutableList<T>.() -> R): R {
   return  Snapshot.withMutableSnapshot { action(this) }
}

