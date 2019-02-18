package com.joaquimverges.helium.core.retained

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Convenient way to access retained Presenters.
 * Presenters instantiated this way will be persisted across configuration changes,
 * and only destroys when the activity is really destroyed.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedPresenter(): P {
    return ViewModelProviders.of(this).get(P::class.java)
}

inline fun <reified P : ViewModel> Fragment.getRetainedPresenter(): P {
    return ViewModelProviders.of(this).get(P::class.java)
}

/**
 * @param factory lambda to construct the Presenter yourself if it has non-empty constructors.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedPresenter(noinline factory: (Class<P>) -> P): P {
    return ViewModelProviders.of(this, PresenterFactory(P::class.java, factory)).get(P::class.java)
}

inline fun <reified P : ViewModel> Fragment.getRetainedPresenter(noinline factory: (Class<P>) -> P): P {
    return ViewModelProviders.of(this, PresenterFactory(P::class.java, factory)).get(P::class.java)
}