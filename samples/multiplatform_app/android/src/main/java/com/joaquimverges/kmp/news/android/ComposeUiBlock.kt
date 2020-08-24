package com.joaquimverges.kmp.news.android

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.setContent
import com.joaquimverges.helium.core.UiBlock
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.core.state.BlockState

abstract class ComposeUiBlock<S : BlockState, E : BlockEvent>(context: Context) :
    UiBlock<S, E>(FrameLayout(context)) {

    private val stateModel = mutableStateOf<S?>(null)

    init {
        (view as ViewGroup).setContent(Recomposer.current()) {
            Content(stateModel.value)
        }
    }

    override fun render(state: S) {
        stateModel.value = state
    }

    @Composable
    abstract fun Content(model: S?)
}