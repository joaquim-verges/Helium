package com.jv.news.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jv.news.R
import com.jv.news.data.model.SourcesCategoryGroup
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

/**
 * @author joaquim
 */
class SourcesCategoryListItem(root: View) : GroupViewHolder(root) {

    companion object {
        fun create(context: Context, parent: ViewGroup?): SourcesCategoryListItem {
            return SourcesCategoryListItem(LayoutInflater.from(context).inflate(R.layout.view_category, parent, false))
        }
    }

    val name: TextView = root.findViewById(R.id.category_name)

    fun setCategoryGroup(group: SourcesCategoryGroup) {
        name.text = group.title
    }
}
