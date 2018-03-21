package com.jv.news.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.CheckedTextView
import com.jv.news.R
import com.jv.news.data.model.ArticleSource
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder

/**
 * @author joaquim
 */
class SourceViewHolder(root: View) : CheckableChildViewHolder(root) {

    companion object {
        fun create(context: Context, parent: ViewGroup?): SourceViewHolder {
            return SourceViewHolder(LayoutInflater.from(context).inflate(R.layout.view_source, parent, false))
        }
    }

    val name: CheckedTextView = root.findViewById(R.id.source_name)

    fun setSource(source: ArticleSource) {
        name.text = source.name
    }

    override fun getCheckable(): Checkable = name
}