package com.joaquimverges.helium.ui.state

import com.joaquimverges.helium.core.state.BlockState

/**
 * Describes the different ViewState while loading data asynchronously into a list.
 */
sealed class ListBlockState<T> : BlockState {
    class Init<T>: ListBlockState<T>()
    class Loading<T> : ListBlockState<T>()
    class Empty<T> : ListBlockState<T>()
    data class Error<T>(val error: Throwable) : ListBlockState<T>()
    data class DataReady<T>(val data: T) : ListBlockState<T>()
}
