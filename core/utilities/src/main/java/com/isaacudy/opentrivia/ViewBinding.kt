package com.isaacudy.opentrivia

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Original from https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.onEvent(Lifecycle.Event.ON_CREATE) {
            fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                viewLifecycleOwner.onEvent(Lifecycle.Event.ON_DESTROY){
                    binding = null
                }
            }
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

private fun LifecycleOwner.onEvent(event: Lifecycle.Event, block: () -> Unit) {
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, incomingEvent: Lifecycle.Event) {
            if (incomingEvent != event) return
            block()
        }
    })
}

inline fun  <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this, run {
    val bindMethod = T::class.java.getMethod("bind", View::class.java)
    return@run {
        bindMethod.invoke(T::class.java, it) as T
    }
})