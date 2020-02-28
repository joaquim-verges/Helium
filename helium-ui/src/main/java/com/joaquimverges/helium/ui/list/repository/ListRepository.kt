package com.joaquimverges.helium.ui.list.repository

import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Interface to implement for any data provider used by a [com.joaquimverges.helium.ui.list.ListLogic].
 * Here is where the actual data fetching/caching logic lives.
 */
interface ListRepository<T> {
    fun getData(): Single<T>
    fun paginate(): Maybe<T> = Maybe.empty()
}