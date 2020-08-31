package com.joaquimverges.helium.core

import kotlinx.coroutines.CoroutineDispatcher

expect val Main: CoroutineDispatcher
expect val Background: CoroutineDispatcher
