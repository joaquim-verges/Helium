package com.joaquimverges.helium.util

import android.arch.lifecycle.Lifecycle
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Useful extension functions for Rx classes
 */

fun <T> Single<T>.async(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.autoDispose(lifecycle: Lifecycle) : ObservableSubscribeProxy<T> {
    val scope = AndroidLifecycleScopeProvider.from(lifecycle)
    return `as`(AutoDispose.autoDisposable<T>(scope))
}
