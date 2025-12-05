package io.kelly.paging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import io.kelly.util.withMutableSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class PagingImpl<T, V>(
    internal val scope: CoroutineScope,
    internal val context: CoroutineContext,
    pagingStart: PagingStart = PagingStart.START,
    internal val initialKey: T? = null,
    internal val config: PagingConfig = PagingConfig(),
    internal val load: suspend (LoadParams<T>) -> LoadResult<T, V>,
) : Paging<V>, CoroutineScope by scope {

    private var _nextKey: T? = initialKey

    private val _mutex = Mutex()

    private var _refreshJob: Job? = null
    private var _appendJob: Job? = null

    private val _isRefreshRunning get() = _refreshJob?.isActive == true
    private val _isAppendRunning get() = _appendJob?.isActive == true

    private var _pagingStart = pagingStart
    internal val list = mutableStateListOf<V>()
    override var refreshState: LoadState by mutableStateOf(LoadState.NotLoading(false))
    override var loadMoreState: LoadState by mutableStateOf(LoadState.NotLoading(false))


    override var isInitialLoad: Boolean by mutableStateOf(true)

    init {
        if (_pagingStart == PagingStart.START) {
            refresh()
        }
    }

    override fun start() {
        if (_pagingStart == PagingStart.LAZY) {
            _pagingStart = PagingStart.START
            refresh()
        }
    }

    private suspend fun delayTimeMillis(startTimeMillis: Long) {
        val useTimeMillis = System.currentTimeMillis() - startTimeMillis
        if (useTimeMillis < config.enableMinLoadTime) {
            delay(config.enableMinLoadTime - useTimeMillis)
        }
    }

    override fun refresh() {
        if (_isRefreshRunning) return

        _appendJob?.cancel()
        _refreshJob = launch {
            _mutex.withLock {
                val startTimeMillis = System.currentTimeMillis()
                try {
                    refreshState = LoadState.Loading
                    loadMoreState = LoadState.NotLoading(false)
                    val result = withContext(context) {
                        load(
                            LoadParams(
                                key = initialKey,
                                pageSize = config.pageSize,
                            )
                        )
                    }
                    delayTimeMillis(startTimeMillis)
                    isInitialLoad = false
                    _nextKey = result.nextKey
                    val endOfPaginationReached = _nextKey == null
                    refreshState = LoadState.NotLoading(endOfPaginationReached)
                    if (endOfPaginationReached) {
                        loadMoreState = LoadState.NotLoading(true)
                    }

                    Snapshot.withMutableSnapshot {
                        list.clear()
                        list.addAll(result.data)
                    }
                } catch (e: Exception) {
                    delayTimeMillis(startTimeMillis)
                    refreshState = LoadState.Error(e)
                }
            }
        }
    }

    private fun load() {
        if (_isRefreshRunning || _isAppendRunning || _mutex.isLocked) return
        val currentLoadMore = loadMoreState
        if (currentLoadMore is LoadState.NotLoading && currentLoadMore.endOfPaginationReached) return
        if (currentLoadMore is LoadState.Error) return
        _appendJob = launch {
            _mutex.withLock {
                try {
                    loadMoreState = LoadState.Loading
                    val result = withContext(context) {
                        load(
                            LoadParams(
                                key = _nextKey,
                                pageSize = config.pageSize,
                            )
                        )
                    }
                    _nextKey = result.nextKey
                    loadMoreState = LoadState.NotLoading(_nextKey == null)
                    Snapshot.withMutableSnapshot {
                        list.addAll(result.data.orEmpty())
                    }
                } catch (e: Exception) {
                    loadMoreState = LoadState.Error(e)
                }
            }
        }
    }

    override fun retry() {
        if (_isRefreshRunning) return
        if (refreshState is LoadState.Error) {
            refresh()
            return
        }

        if (loadMoreState is LoadState.Error) {
            loadMoreState = LoadState.NotLoading(false)
            load()
        }
    }

    override fun get(index: Int): V {
        if (index + config.prefetchDistance >= list.count()) {
            load()
        }
        return list[index]
    }

    override fun peek(index: Int): V = list[index]
    override val size: Int get() = list.size
    override fun <R> withMutableSnapshot(action: MutableList<V>.() -> R): R {
        return list.withMutableSnapshot(action)
    }


    override fun itemKey(block: ((V) -> Any?)?): ((Int) -> Any)? {
        return block?.let { { index -> if (index in list.indices) it(list[index]).toString() else index } }
    }

}