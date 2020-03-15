package com.joaquimverges.helium.core.util

import android.view.View
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ObservableSubscribeProxy
import com.uber.autodispose.android.ViewScopeProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Useful extension functions for Rx classes
 */

fun <T> Single<T>.async(): Single<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.async(): Maybe<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.async(): Flowable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.async(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Completable.async(): Completable {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun <T> Observable<T>.autoDispose(lifecycle: Lifecycle): ObservableSubscribeProxy<T> {
    val scope = AndroidLifecycleScopeProvider.from(lifecycle)
    return `as`(AutoDispose.autoDisposable<T>(scope))
}

fun <T> Observable<T>.autoDispose(view: View): ObservableSubscribeProxy<T> {
    val scope = ViewScopeProvider.from(view)
    return `as`(AutoDispose.autoDisposable<T>(scope))
}
