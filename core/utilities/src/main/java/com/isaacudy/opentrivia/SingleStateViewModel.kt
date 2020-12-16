package com.isaacudy.opentrivia

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect

abstract class SingleStateViewModel<T: Any> : ViewModel() {
    abstract val initialState: T

    private val stateFlow by lazy {
        MutableStateFlow(initialState)
    }

    val state by lazy {
        stateFlow.asStateFlow()
    }

    protected fun updateState(block: (T) -> T) {
        stateFlow.value = block(stateFlow.value)
    }
}

fun <T: Any> SingleStateViewModel<T>.observe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launchWhenCreated {
        state.collect {
            block(it)
        }
    }
}