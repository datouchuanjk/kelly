package io.kelly.ui.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type

fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    onClick: () -> Unit
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        role = null,
        enabled = enabled,
        onClick = onClick
    )
}

fun Modifier.onKeyDelEvent(
    enabled: Boolean = true,
    block: () -> Unit
): Modifier {
    if (!enabled) return this
    return onKeyEvent { event ->
        if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
            block()
            true
        } else {
            false
        }
    }
}