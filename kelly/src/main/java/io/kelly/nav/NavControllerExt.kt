package io.kelly.nav

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder


inline fun <reified T : Any> NavController.isCurrent(): Boolean {
    return currentBackStackEntry?.destination?.hasRoute<T>() == true
}

private const val DEFAULT_RESULT_KEY = "app_navigation_result_key"


fun <T> NavHostController.navigateForResult(
    route: Any,
    key: String = DEFAULT_RESULT_KEY,
    onResult: (T) -> Unit
) {

    val currentEntry = currentBackStackEntry ?: return
    val liveData = currentEntry.savedStateHandle.getLiveData<T?>(key)
    liveData.observe(currentEntry) { result ->
        if (result != null) {
            onResult(result)
            currentEntry.savedStateHandle.remove<T>(key)
            liveData.removeObservers(currentEntry)
        }
    }
    navigate(route)
}

fun <T> NavHostController.setResult(data: T, key: String = DEFAULT_RESULT_KEY) {
    previousBackStackEntry?.savedStateHandle?.getLiveData<T?>(key, null)?.value = data
}

fun NavHostController.navigateReplace(
    route: Any,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    val currentEntry = currentBackStackEntry ?: return
    navigate(route) {
        popUpTo(currentEntry.destination.id) {
            inclusive = true
        }
        builder?.invoke(this)
    }
}

fun NavHostController.navigateClear(
    route: Any,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
        builder?.invoke(this)
    }
}