package com.joaquimverges.helium.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.joaquimverges.helium.core.retained.getRetainedLogicBlock

@Composable
inline fun <reified L : ViewModel> logicBlock(): L {
    return LocalContext.current.getRetainedLogicBlock()
}

@Composable
inline fun <reified L : ViewModel> logicBlock(noinline factory: (Class<L>) -> L): L {
    return LocalContext.current.getRetainedLogicBlock(factory)
}
