package com.joaquimverges.helium.state

/**
 * Describes the different ViewState while loading data asynchronously.
 */
sealed class NetworkViewState<T> : ViewState {
    class Loading<T> : NetworkViewState<T>()
    class Empty<T> : NetworkViewState<T>()
    data class Error<T>(val error: Throwable) : NetworkViewState<T>()
    data class DataReady<T>(val data: T) : NetworkViewState<T>()
}
