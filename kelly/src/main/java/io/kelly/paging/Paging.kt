package io.kelly.paging

interface Paging<T> {
    val isInitialLoad: Boolean
    val refreshState: LoadState
    val loadMoreState: LoadState
    fun start()
    fun refresh()
    fun retry()
    fun itemKey(block: ((T) -> Any?)?): ((index: Int) -> Any)?
    operator fun get(index: Int): T
    fun peek(index: Int): T
    val size: Int

    fun <R> withMutableSnapshot(action: MutableList<T>.() -> R): R
}