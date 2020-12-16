package com.isaacudy.opentrivia

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

class SafeJob internal constructor(
    private val throwableListener: ThrowableListener,
    private val job: Job
): Job by job {
    fun onError(block: (Throwable) -> Unit): Job {
        throwableListener.listener = block
        return this
    }
}

fun CoroutineScope.launchCatching(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): SafeJob {

    val throwableListener = ThrowableListener()
    val job = launch(
        context = context,
        start = start,
    ) {
        try {
            block()
        }
        catch (t: Throwable) {
            withContext(Dispatchers.Main) {
                throwableListener.throwable = t
            }
        }
    }

    return SafeJob(throwableListener, job)
}