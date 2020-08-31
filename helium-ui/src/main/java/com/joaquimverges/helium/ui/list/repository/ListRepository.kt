package com.joaquimverges.helium.ui.list.repository

/**
 * Interface to implement for any data provider used by a [com.joaquimverges.helium.ui.list.ListLogic].
 * Here is where the actual data fetching/caching logic lives.
 */
interface ListRepository<T> {
    suspend fun getFirstPage(): T
    suspend fun paginate(): T? = null
}
