package com.jv.news.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.joaquimverges.helium.ui.viewdelegate.BaseRecyclerViewItem
import com.jv.news.R
import com.jv.news.data.model.Article
import com.jv.news.view.event.ArticleEvent

/**
 * @author jverges
 */
class ArticleGridItemViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    itemView: View = inflater.inflate(R.layout.view_article_item, parent, false)
) : BaseRecyclerViewItem<Article, ArticleEvent>(itemView) {

    private val mediaView: ImageView = findView(R.id.article_media)
    private val titleView: TextView = findView(R.id.article_title)
    private val sourcesView: TextView = findView(R.id.article_source)
    private val colors = intArrayOf(
        android.R.color.holo_red_dark, android.R.color.holo_green_dark,
        android.R.color.holo_blue_dark, android.R.color.holo_orange_dark, android.R.color.holo_purple
    )

    override fun bind(data: Article) {
        val color = randomColor(data)
        mediaView.setBackgroundColor(color)
        titleView.text = data.title
        sourcesView.text = data.source?.name
        ContextCompat.getDrawable(context, R.drawable.label_background)?.let {
            DrawableCompat.setTint(it, color)
            sourcesView.background = it
        }
        Glide.with(context).load(data.urlToImage).into(mediaView)
        itemView.setOnClickListener {
            pushEvent(ArticleEvent.Clicked(context, data))
        }
        itemView.setOnLongClickListener {
            pushEvent(ArticleEvent.LongPressed(context, data))
            true
        }
    }

    private fun randomColor(article: Article): Int {
        return ContextCompat.getColor(itemView.context, colors[article.source?.let { Math.abs(it.hashCode()) % colors.size } ?: 0])
    }
}
