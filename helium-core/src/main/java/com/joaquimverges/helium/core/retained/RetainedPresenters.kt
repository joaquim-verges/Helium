package com.joaquimverges.helium.core.retained

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Convenient way to access retained Presenters.
 * Presenters instantiated this way will be persisted across configuration changes,
 * and only destroys when the activity is really destroyed.
 *
 * @param activity the Activity to bind this Presenter to
 * @param clazz the class of the Presenter to instantiate.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedPresenter(): P = ViewModelProviders.of(this).get(P::class.java)

/**
 * @param factory optional lambda to construct the Presenter yourself if it has non-empty constructors.
 */
inline fun <reified P : ViewModel> FragmentActivity.getRetainedPresenter(noinline factory: (Class<P>) -> P): P = ViewModelProviders.of(this, PresenterFactory(P::class.java, factory)).get(P::class.java)

inline fun <reified P : ViewModel> Fragment.getRetainedPresenter(): P = ViewModelProviders.of(this).get(P::class.java)
