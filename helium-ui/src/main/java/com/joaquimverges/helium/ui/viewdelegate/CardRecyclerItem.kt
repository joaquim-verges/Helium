package com.joaquimverges.helium.ui.viewdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.card.MaterialCardView
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.ui.R

/**
 * RecyclerItem wrapped around a [MaterialCardView]
 * @param inflater: a valid LayoutInflater
 * @param container: the container (generally a recyclerView)
 * @param contentView: the view to be added inside the card
 * @param cardLayout: Default card layout
 * @param cardViewConfig: lambda to configure the [MaterialCardView]
 */
abstract class CardRecyclerItem<in T, V : BlockEvent>(
    contentView: View,
    inflater: LayoutInflater,
    container: ViewGroup,
    val cardLayout: MaterialCardView = inflater.inflate(R.layout.view_card, container, false) as MaterialCardView,
    cardViewConfig: ((MaterialCardView) -> Unit)? = null
) : BaseRecyclerViewItem<T, V>(cardLayout) {

    constructor(
        @LayoutRes contentLayoutResId: Int,
        inflater: LayoutInflater,
        container: ViewGroup,
        cardLayout: MaterialCardView = inflater.inflate(R.layout.view_card, container, false) as MaterialCardView,
        cardViewConfig: ((MaterialCardView) -> Unit)? = null
    ) : this(inflater.inflate(contentLayoutResId, cardLayout, false), inflater, container, cardLayout, cardViewConfig)

    init {
        cardViewConfig?.invoke(cardLayout)
        cardLayout.addView(contentView)
    }
}