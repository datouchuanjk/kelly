package io.kelly.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <Value : Any> buildOffsetPaging(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    pagingStart: PagingStart = PagingStart.START,
    config: PagingConfig = PagingConfig(),
    initialKey: Int = 1,
    hasNext: ((List<Value>) -> Boolean)? = { it.isNotEmpty() },
    load: suspend (page: Int) -> List<Value>
) = buildPaging(
    coroutineScope = coroutineScope,
    context = context,
    pagingStart = pagingStart,
    initialKey = initialKey,
    config = config,
) {
    val key = it.key ?: initialKey
    val list = load(key)
    val nextKey = if ((hasNext?.invoke(list) ?: false)) key + 1 else null
    LoadResult(
        nextKey,
        list
    )
}


fun <Key, Value> buildPaging(
    coroutineScope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    pagingStart: PagingStart = PagingStart.START,
    config: PagingConfig = PagingConfig(),
    initialKey: Key? = null,
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        scope = coroutineScope,
        context = context,
        pagingStart = pagingStart,
        initialKey = initialKey,
        config = config,
        load = load,
    )
}