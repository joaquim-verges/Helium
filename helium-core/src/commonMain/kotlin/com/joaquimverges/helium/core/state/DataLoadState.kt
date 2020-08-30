package com.joaquimverges.helium.core.state

/**
 * Describes the different BlockStates while loading data asynchronously
 */
sealed class DataLoadState<T> : BlockState {
    class Init<T> : DataLoadState<T>()
    class Loading<T> : DataLoadState<T>()
    class Empty<T> : DataLoadState<T>()
    data class Error<T>(val error: Throwable) : DataLoadState<T>()
    data class Ready<T>(val data: T) : DataLoadState<T>()
}
