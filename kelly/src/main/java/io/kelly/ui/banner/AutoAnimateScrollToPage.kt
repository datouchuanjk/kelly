package io.kelly.ui.banner

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PagerState.AutoAnimateScrollToPage(
    interval: Long = 3000,
    animationSpec: AnimationSpec<Float> = tween(500)
) {
    val isDragged by interactionSource.collectIsDraggedAsState()
    if (isDragged || pageCount <= 1) return
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(interval)
            if (pageCount <= 1) break
            val nextPage = currentPage + 1
            if (nextPage < pageCount) {
                animateScrollToPage(nextPage, animationSpec = animationSpec)
            } else {
                scrollToPage(0)
            }
        }
    }
}