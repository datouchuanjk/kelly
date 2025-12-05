package io.kelly.util

import android.os.Bundle
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SAVED_STATE_REGISTRY_OWNER_KEY
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.enableSavedStateHandles
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

class ScopedViewModelOwner(
    savedState: Bundle? = null
) : ViewModelStoreOwner, SavedStateRegistryOwner, LifecycleOwner,
    HasDefaultViewModelProviderFactory {

    private val _lifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateController = SavedStateRegistryController.create(this)
    private val _viewModelStore = ViewModelStore()

    private var _pendingDefaultArgs: Bundle? = null

    fun setBundle(args: Bundle?) {
        if (_pendingDefaultArgs != args) {
            _pendingDefaultArgs = args
        }
    }

    private val _factory by lazy {
        SavedStateViewModelFactory(
            application = null,
            owner = this,
            defaultArgs = null
        )
    }

    init {
        _savedStateController.performAttach()
        enableSavedStateHandles()
        _savedStateController.performRestore(savedState)

        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override val defaultViewModelCreationExtras: CreationExtras
        get() {
            val extras = MutableCreationExtras().apply {
                set(SAVED_STATE_REGISTRY_OWNER_KEY, this@ScopedViewModelOwner)
                set(VIEW_MODEL_STORE_OWNER_KEY, this@ScopedViewModelOwner)

                _pendingDefaultArgs?.let { args ->
                    set(DEFAULT_ARGS_KEY, args)
                    _pendingDefaultArgs = null
                }
            }
            return extras
        }

    override val viewModelStore: ViewModelStore get() = _viewModelStore
    override val savedStateRegistry: SavedStateRegistry get() = _savedStateController.savedStateRegistry
    override val lifecycle: Lifecycle get() = _lifecycleRegistry
    override val defaultViewModelProviderFactory: ViewModelProvider.Factory get() = _factory

    fun saveState(): Bundle {
        val outBundle = Bundle()
        _savedStateController.performSave(outBundle)
        return outBundle
    }

    fun clear() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        viewModelStore.clear()
        _pendingDefaultArgs = null
    }
}