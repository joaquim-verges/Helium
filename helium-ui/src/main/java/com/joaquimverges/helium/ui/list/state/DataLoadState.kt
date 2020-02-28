package com.joaquimverges.helium.ui.list.state

import com.joaquimverges.helium.core.state.BlockState

/**
 * Describes the different ViewState while loading data asynchronously into a list.
 */
sealed class DataLoadState<T> : BlockState {
    class Init<T>: DataLoadState<T>()
    class Loading<T> : DataLoadState<T>()
    class Empty<T> : DataLoadState<T>()
    data class Error<T>(val error: Throwable) : DataLoadState<T>()
    data class Ready<T>(val data: T) : DataLoadState<T>()
}
