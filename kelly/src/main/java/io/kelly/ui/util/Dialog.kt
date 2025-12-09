package io.kelly.ui.util

import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

val dialogWindow: Window?
    @Composable
    get() = (LocalView.current.parent as? DialogWindowProvider)?.window

private fun Window.updateAttributes(block: WindowManager.LayoutParams.() -> Unit) {
    val attributes = this.attributes
    block(attributes)
    this.attributes = attributes
}


@Composable
fun ConfigureDialogWindow(configure: WindowManager.LayoutParams.() -> Unit) {
    val window = dialogWindow
    if (window != null) {
        SideEffect {
            window.updateAttributes(configure)
        }
    }
}