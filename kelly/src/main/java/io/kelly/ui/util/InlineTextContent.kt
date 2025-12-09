package io.kelly.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun inlineTextContent(
    @DrawableRes id: Int,
    width: Dp,
    height: Dp,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
    placeholderVerticalAlign: PlaceholderVerticalAlign = PlaceholderVerticalAlign.Center
): InlineTextContent {

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    val widthSp = with(density) {
        (width + paddingValues.calculateLeftPadding(layoutDirection) +
                paddingValues.calculateRightPadding(layoutDirection)).toSp()
    }
    val heightSp = with(density) {
        (height + paddingValues.calculateTopPadding() +
                paddingValues.calculateBottomPadding()).toSp()
    }

    return InlineTextContent(
        placeholder = Placeholder(
            width = widthSp,
            height = heightSp,
            placeholderVerticalAlign = placeholderVerticalAlign
        ),
        children = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    colorFilter = tint?.let { ColorFilter.tint(it) }
                )
            }
        }
    )
}