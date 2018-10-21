package com.joaquimverges.helium.ui.state

import com.joaquimverges.helium.core.state.ViewState

/**
 * Describes the different ViewState while loading data asynchronously into a list.
 */
sealed class ListViewState<T> : ViewState {
    class Loading<T> : ListViewState<T>()
    class Empty<T> : ListViewState<T>()
    data class Error<T>(val error: Throwable) : ListViewState<T>()
    data class DataReady<T>(val data: T) : ListViewState<T>()
}
