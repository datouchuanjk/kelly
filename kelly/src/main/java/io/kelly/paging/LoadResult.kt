package io.kelly.paging

data class LoadResult<Key, Value>(
    val nextKey: Key?,
    val data: List<Value>
){
    companion object{
        fun<Key,Value> empty() = LoadResult<Key,Value>(null,emptyList())
    }
}
