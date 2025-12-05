package io.kelly.paging

data class LoadParams<Key>(
    val key: Key?,
    val pageSize: Int,
)
