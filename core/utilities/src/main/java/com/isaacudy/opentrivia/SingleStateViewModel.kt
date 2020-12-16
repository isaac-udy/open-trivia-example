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

    protected fun updateState(block: T.() -> T) {
        stateFlow.value = block(stateFlow.value)
    }

    fun <R: Any> CoroutineListener<R>.updateStateOnLaunch(block: T.() -> T): CoroutineListener<R> =
        onLaunch { updateState { block() } }

    fun <R: Any> CoroutineListener<R>.updateStateOnComplete(block: T.(R) -> T): CoroutineListener<R> =
        onComplete { updateState { block(it) } }

    fun <R: Any> CoroutineListener<R>.updateStateOnError(block: T.(Throwable) -> T): CoroutineListener<R> =
        onError { updateState { block(it) } }
}

fun <T: Any> SingleStateViewModel<T>.observe(lifecycleOwner: LifecycleOwner, block: (T) -> Unit) {
    lifecycleOwner.lifecycleScope.launchWhenCreated {
        state.collect {
            block(it)
        }
    }
}