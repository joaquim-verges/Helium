package com.joaquimverges.helium.ui.repository

import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Interface to implement for any data provider used by a Presenter.
 * Here is where the actual data fetching/caching logic lives.
 */
interface BaseRepository<T> {
    fun getData(): Single<T>
    fun paginate(): Maybe<T> = Maybe.empty()
}