package com.joaquimverges.helium.retained

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

object RetainedPresenters {

    /**
     * Convenient way to access retained Presenters.
     * Presenters instantiated this way will be persisted across configuration changes,
     * and only destroys when the activity is really destroyed.
     *
     * @param activity the Activity to bind this Presenter to
     * @param clazz the class of the Presenter to instantiate.
     */
    fun <P : ViewModel> get(activity: FragmentActivity, clazz: Class<P>): P
            = ViewModelProviders.of(activity).get(clazz)

    /**
     * @param factory optional lambda to construct the Presenter yourself if it has non-empty constructors.
     */
    fun <P : ViewModel> get(activity: FragmentActivity, clazz: Class<P>, factory: (Class<P>) -> P): P
            = ViewModelProviders.of(activity, PresenterFactory(clazz, factory)).get(clazz)

    fun <P : ViewModel> get(fragment: Fragment, clazz: Class<P>): P
            = ViewModelProviders.of(fragment).get(clazz)
}