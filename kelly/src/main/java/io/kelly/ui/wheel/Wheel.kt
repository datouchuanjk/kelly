package io.kelly.ui.wheel


import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import kotlin.math.absoluteValue

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RowScope.Wheel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    itemCount: Int,
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.DEFAULT_VISIBLE_COUNT,
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(state),
    dividerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    animator: GraphicsLayerScope.(Float) -> Unit = remember { WheelDefaults.animator },
    content: @Composable (Int) -> Unit
) {
    require(visibleCount % 2 != 0) { "visibleCount must be odd" }
    LazyColumn(
        modifier = modifier
            .height(itemHeight * visibleCount)
            .weight(1f)
            .drawWithContent {
                drawContent()
                val dividerYTop = size.height / 2f - itemHeight.toPx() / 2f
                val dividerYBottom = size.height / 2f + itemHeight.toPx() / 2f
                drawLine(
                    color = dividerColor,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(0f, dividerYTop),
                    end = Offset(size.width, dividerYTop)
                )
                drawLine(
                    color = dividerColor,
                    strokeWidth = 1.dp.toPx(),
                    start = Offset(0f, dividerYBottom),
                    end = Offset(size.width, dividerYBottom)
                )
            },
        state = state,
        contentPadding = PaddingValues(vertical = (itemHeight * (visibleCount - 1)) / 2),
        flingBehavior = flingBehavior,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(itemCount, key = { it }) { index ->
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(itemHeight)
                    .graphicsLayer {
                      val visibleItem =state.layoutInfo.visibleItemsInfo.find { it.index == index }
                        if (visibleItem != null) {
                            val offset = visibleItem.offset.absoluteValue
                            val total = ((visibleCount-1) / 2) * size.height
                            val ratio = (offset / total).coerceIn(0f, 1f)
                            animator(ratio)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}
