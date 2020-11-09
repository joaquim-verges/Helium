package com.joaquimverges.helium.core.retained

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalStateException

/**
 * Convenient way to access retained LogicBlocks.
 * LogicBlocks instantiated this way will be persisted across configuration changes,
 * and only destroyed when the activity is really destroyed.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedLogicBlock(): P {
    return ViewModelProvider(this)[P::class.java]
}

inline fun <reified P : ViewModel> Fragment.getRetainedLogicBlock(): P {
    return ViewModelProvider(this)[P::class.java]
}

inline fun <reified P : ViewModel> Context.getRetainedLogicBlock(): P {
    return when (this) {
        is FragmentActivity -> {
            ViewModelProvider(this)[P::class.java]
        }
        is Fragment -> {
            ViewModelProvider(this)[P::class.java]
        }
        else -> throw IllegalStateException("Retained Logic Blocks not supported for this context: $this")
    }
}

/**
 * @param factory lambda to construct the LogicBlock yourself if it has non-empty constructors.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedLogicBlock(noinline factory: (Class<P>) -> P): P {
    return ViewModelProvider(this, LogicBlockFactory(P::class.java, factory)).get(P::class.java)
}

inline fun <reified P : ViewModel> Fragment.getRetainedLogicBlock(noinline factory: (Class<P>) -> P): P {
    return ViewModelProvider(this, LogicBlockFactory(P::class.java, factory)).get(P::class.java)
}

inline fun <reified P : ViewModel> Context.getRetainedLogicBlock(noinline factory: (Class<P>) -> P): P {
    return when (this) {
        is FragmentActivity -> {
            ViewModelProvider(this, LogicBlockFactory(P::class.java, factory)).get(P::class.java)
        }
        is Fragment -> {
            ViewModelProvider(this, LogicBlockFactory(P::class.java, factory)).get(P::class.java)
        }
        else -> throw IllegalStateException("Retained Logic Blocks not supported for this context: $this")
    }
}
