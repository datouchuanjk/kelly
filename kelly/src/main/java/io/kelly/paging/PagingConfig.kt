package io.kelly.paging

import androidx.annotation.IntRange

data class PagingConfig(
    val pageSize: Int = 20,
    @param:IntRange(from = 1)
    val prefetchDistance: Int = 1,
    val enableMinLoadTime: Long =1000,
) {
    init {
        require(prefetchDistance >= 1)
    }
}
