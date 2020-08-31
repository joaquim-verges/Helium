package com.joaquimverges.helium.ui.list.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.joaquimverges.helium.core.event.BlockEvent
import com.joaquimverges.helium.ui.R

/**
 * ListItem wrapped around a [MaterialCardView] with convenience methods to bind a simple material card
 * @param inflater: a valid LayoutInflater
 * @param container: the container (generally a recyclerView),
 * @param contentImageHeight: override the height (in pixels) of the image inside the card (default 160dp)
 * @param contentView: override the view to be added inside the card
 * @param cardViewConfig: optional lambda to configure the [MaterialCardView]
 */
abstract class ContentCardListItem<in T, V : BlockEvent>(
    inflater: LayoutInflater,
    container: ViewGroup,
    contentImageHeight: Int = inflater.context.resources.getDimensionPixelSize(R.dimen.default_card_image_height),
    contentView: View = inflater.inflate(R.layout.view_content_card_layout, container, false),
    cardViewConfig: ((MaterialCardView) -> Unit)? = null
) : CardListItem<T, V>(contentView, inflater, container, cardViewConfig = cardViewConfig) {

    private val img = findView<ImageView>(R.id.content_card_image)
    private val title = findView<TextView>(R.id.content_card_title)
    private val subtitle = findView<TextView>(R.id.content_card_subtitle)

    init {
        img.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentImageHeight)
    }

    abstract fun bindTitle(data: T, view: TextView)
    abstract fun bindSubtitle(data: T, view: TextView)
    abstract fun bindImage(data: T, view: ImageView)

    override fun bind(data: T) {
        bindTitle(data, title)
        bindSubtitle(data, subtitle)
        bindImage(data, img)
    }
}
