package com.isaacudy.opentrivia

import android.util.Log
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class ThrowableListener {
    internal var throwable: Throwable? = null
        set(value) {
            field = value
            if(value == null) return
            listener?.let { it(value) }
        }

    internal var listener: ((Throwable) -> Unit)? = null
        set(value) {
            if(value == null) return
            if(field != null) throw IllegalStateException("Can't re-set the throwable listener")
            field = value
            throwable?.let(value)
        }

}

data class CoroutineListener <T> internal constructor(
    val coroutineScope: CoroutineScope,
    val context: CoroutineContext = EmptyCoroutineContext,
    val start: CoroutineStart = CoroutineStart.DEFAULT,
    val block: suspend CoroutineScope.() -> T,

    private val throwableListener: (Throwable) -> Unit = { },
    private val completionListener: (T) -> Unit = {  },
    private val launchListener: () -> Unit = {  },
) {
    private var isLaunched: Boolean = false

    fun onError(block: (Throwable) -> Unit): CoroutineListener<T> {
        return copy(
            throwableListener = block
        )
    }

    fun onLaunch(block: () -> Unit): CoroutineListener<T> {
        return copy(
            launchListener = block
        )
    }

    fun onComplete(block: (T) -> Unit): CoroutineListener<T> {
        return copy(
            completionListener = block
        )
    }

    fun launch(): Job {
        onLaunch(launchListener)

        return coroutineScope.launch(
            context = context,
            start = start,
        ) {
            try {
                val result = block()
                completionListener(result)
            }
            catch (t: Throwable) {
                Log.e("CoroutineListener", "Received error", t)
                withContext(Dispatchers.Main) {
                    throwableListener(t)
                }
            }
        }
    }
}

fun <T: Any> CoroutineScope.asListener(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): CoroutineListener<T> {
    return CoroutineListener(this, context, start, block)
}