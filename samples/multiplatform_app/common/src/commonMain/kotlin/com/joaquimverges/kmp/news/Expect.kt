package com.joaquimverges.kmp.news

import kotlinx.coroutines.CoroutineDispatcher

internal expect val Main: CoroutineDispatcher

internal expect val Background: CoroutineDispatcher