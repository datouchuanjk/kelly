package io.kelly.paging.ui


import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kelly.R
import io.kelly.util.compose.clickableNoRipple

interface PagingStateContent {

    @Composable
    fun Skeleton()

    @Composable
    fun Failure(retry: () -> Unit)

    @Composable
    fun Empty(refresh:()-> Unit)

    @Composable
    fun Loading()

    @Composable
    fun LoadError(retry: () -> Unit)

    @Composable
    fun NotLoading()
}

class DefaultPagingStateContent(isInDark: Boolean) : PagingStateContent {
    private val color = if(isInDark) Color.White else Color(0xff333333)
    @Composable
    override fun Skeleton() {
        Column(
            modifier = Modifier
                .height(150.dp)
                .clickableNoRipple {},
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val transition = rememberInfiniteTransition(label = "loading")
            val rotate by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000)
                ),
                label = "rotate"
            )
            Icon(
                tint = color,
                modifier = Modifier.rotate(rotate),
                painter = painterResource(R.drawable.paging_ic_loading),
                contentDescription = null
            )
            Spacer(Modifier.height(15.dp))
            Text(
                stringResource(R.string.paging_loading),
                fontSize = 12.sp,
                color =color,
            )
        }
    }

    @Composable
    override fun Failure(retry: () -> Unit) {
        Column(
            modifier = Modifier
                .height(150.dp)
                .clickableNoRipple {},
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.paging_ic_error),
                contentDescription = null,
            )
            Spacer(Modifier.height(15.dp))
            Text(
                stringResource(R.string.paging_retry),
                fontSize = 12.sp,
                color =color,
                modifier = Modifier
                    .border(1.dp, color = color.copy(0.6f), shape = RoundedCornerShape(50))
                    .clickableNoRipple { retry() }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
    }

    @Composable
    override fun Empty(refresh: () -> Unit) {
        Column(
            modifier = Modifier
                .height(150.dp)
                .clickableNoRipple {},
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                tint = color,
                painter = painterResource(R.drawable.paging_ic_empty),
                contentDescription = null,
            )
            Spacer(Modifier.height(15.dp))
            Text(
                stringResource(R.string.paging_refresh),
                fontSize = 12.sp,
                color = color,
                modifier = Modifier
                    .border(1.dp, color =color.copy(0.6f), shape = RoundedCornerShape(50))
                    .clickableNoRipple { refresh() }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
    }

    @Composable
    override fun Loading() {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(30.dp)
        ) {
            val transition = rememberInfiniteTransition(label = "loadingMore")
            val rotate by transition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000)
                ),
                label = "rotate"
            )
            Icon(
                tint = color,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(rotate),
                painter = painterResource(R.drawable.paging_ic_loading),
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.paging_loading),
                fontSize = 12.sp,
                color = color,
            )
        }
    }

    @Composable
    override fun LoadError(retry: () -> Unit) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(30.dp)
                .clickableNoRipple { retry() }
        ) {
            Icon(
                tint = color,
                modifier = Modifier.size(16.dp),
                painter = painterResource(R.drawable.paging_ic_error),
                contentDescription = null,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.paging_retry),
                fontSize = 12.sp,
                color = color,
            )
        }
    }

    @Composable
    override fun NotLoading() {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(30.dp)
        ) {
            Text(
                stringResource(R.string.paging_no_more_data),
                fontSize = 12.sp,
                color = Color(0xffCCCCCC),
            )
        }
    }
}