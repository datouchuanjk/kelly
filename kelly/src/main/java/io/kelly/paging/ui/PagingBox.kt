package io.kelly.paging.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.kelly.paging.LoadState
import io.kelly.paging.Paging

@Composable
fun PagingBox(
    modifier: Modifier = Modifier,
    paging: Paging<*>,
    enablePullToRefresh: Boolean = true,
    isInDark: Boolean = false,
    isRefreshing: Boolean = paging.refreshState is LoadState.Loading,
    onRefresh: () -> Unit = {
        paging.refresh()
    },
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    pagingStateContent: PagingStateContent = DefaultPagingStateContent(isInDark),
    content: @Composable BoxScope.() -> Unit
) {
    if (paging.isInitialLoad || paging.size == 0) {
        when (paging.refreshState) {
            is LoadState.Loading -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    pagingStateContent.Skeleton()
                }
            }

            is LoadState.Error -> {
                Box(modifier = modifier, contentAlignment = Alignment.Center) {
                    pagingStateContent.Failure {
                        paging.retry()
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (paging.size == 0) {
                    Box(modifier = modifier, contentAlignment = Alignment.Center) {
                        pagingStateContent.Empty {
                            paging.refresh()
                        }
                    }
                }
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (enablePullToRefresh) {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    modifier = modifier,
                    state = state,
                    contentAlignment = contentAlignment,
                    indicator = indicator
                ) {
                    content()
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    content()
                }
            }
            when (paging.loadMoreState) {
                is LoadState.Loading -> {
                    pagingStateContent.Loading()
                }

                is LoadState.NotLoading -> {
                    if (paging.loadMoreState.endOfPaginationReached) {
                        pagingStateContent.NotLoading()
                    }
                }

                is LoadState.Error -> {
                    pagingStateContent.LoadError {
                        paging.retry()
                    }
                }
            }
        }
    }
}


