package io.kelly.ui.wheel.linkage


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import io.kelly.ui.wheel.Wheel
import io.kelly.ui.wheel.WheelDefaults

@Composable
fun <T> Linkage3Wheel(
    modifier: Modifier = Modifier,
    provider: LinkageProvider<T>,
    initialIndices: Triple<Int, Int, Int> = Triple(0, 0, 0),
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.DEFAULT_VISIBLE_COUNT,
    dividerColor: Color = Color.LightGray.copy(0.4f),
    onChanged: ((t1:T, t2:T, t3:T?) -> Unit)? = null,
    itemContent: @Composable (item: T, level: Int) -> Unit
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { itemHeight.toPx() }
    fun LazyListState.currentItemIndex(maxSize: Int): Int {
        if (itemHeightPx <= 0) return 0
        val offsetStep = kotlin.math.round(firstVisibleItemScrollOffset / itemHeightPx).toInt()
        val index = firstVisibleItemIndex + offsetStep
        return index.coerceIn(0, (maxSize - 1).coerceAtLeast(0))
    }
    val list1 = remember(provider) {
        provider.getLevel1()
    }
    val state1 = rememberLazyListState(initialIndices.first.coerceIn(list1.indices))
    val item1 by remember {
        derivedStateOf {
            list1.getOrElse(state1.currentItemIndex(list1.size)) { list1.first() }
        }
    }
    val list2 by remember {
        derivedStateOf {
            provider.getLevel2(item1)
        }
    }
    val state2 = rememberLazyListState(initialIndices.second.coerceIn(list2.indices))
    val item2 by remember {
        derivedStateOf {
            list2.getOrElse(state2.currentItemIndex(list2.size)) {
                list2.first()
            }
        }
    }
    LaunchedEffect(list2.size) {
        if (state2.firstVisibleItemIndex >= list2.size) {
            state2.scrollToItem((list2.size - 1).coerceAtLeast(0))
        }
    }

    val list3 by remember {
        derivedStateOf {
            provider.getLevel3(item1, item2)
        }
    }
    val state3 = rememberLazyListState(initialIndices.third.coerceIn(list3.indices))
    LaunchedEffect(list3.size) {
        if (state3.firstVisibleItemIndex >= list3.size) {
            state3.scrollToItem((list3.size - 1).coerceAtLeast(0))
        }
    }
    val item3 by remember {
        derivedStateOf {
            list3.getOrElse(state3.currentItemIndex(list3.size)) { list3.first() }
        }
    }

    LaunchedEffect(item1, item2, item3) {
        onChanged?.invoke(item1, item2, item3)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Wheel(
            modifier = Modifier.weight(1f),
            state = state1,
            itemCount = list1.size,
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            dividerColor = dividerColor
        ) { index ->
            list1.getOrNull(index)?.let { itemContent(it, 0) }
        }

       if(list2.isNotEmpty()){
           Wheel(
               modifier = Modifier.weight(1f),
               state = state2,
               itemCount = list2.size,
               itemHeight = itemHeight,
               visibleCount = visibleCount,
               dividerColor = dividerColor
           ) { index ->
               list2.getOrNull(index)?.let { itemContent(it, 1) }
           }
       }

        if (list3.isNotEmpty()) {
            Wheel(
                modifier = Modifier.weight(1f),
                state = state3,
                itemCount = list3.size,
                itemHeight = itemHeight,
                visibleCount = visibleCount,
                dividerColor = dividerColor
            ) { index ->
                list3.getOrNull(index)?.let { itemContent(it, 2) }
            }
        }
    }
}

