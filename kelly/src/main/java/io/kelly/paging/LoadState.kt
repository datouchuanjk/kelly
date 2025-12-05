package io.kelly.paging

sealed class LoadState(
    open val endOfPaginationReached: Boolean
) {
    data class NotLoading(
        override val endOfPaginationReached: Boolean
    ) : LoadState(endOfPaginationReached)

    object Loading : LoadState(false)

    // 5. Error：也是 data class，自动生成 equals/hashCode/copy
    data class Error(val error: Throwable) : LoadState(false)
}