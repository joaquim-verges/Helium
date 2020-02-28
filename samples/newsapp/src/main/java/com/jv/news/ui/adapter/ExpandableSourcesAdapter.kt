package com.jv.news.ui.adapter

import android.content.Context
import android.view.ViewGroup
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

/**
 * @author joaquim
 */
class ExpandableSourcesAdapter(private val context: Context, categoryGroups: List<SourcesCategoryGroup> = mutableListOf()) :
    CheckableChildRecyclerViewAdapter<SourcesCategoryListItem, SourceListItem>(categoryGroups) {

    override fun onCreateCheckChildViewHolder(parent: ViewGroup?, viewType: Int): SourceListItem = SourceListItem.create(context, parent)

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int) = SourcesCategoryListItem.create(context, parent)

    override fun onBindCheckChildViewHolder(holder: SourceListItem?, flatPosition: Int, group: CheckedExpandableGroup?, childIndex: Int) {
        holder?.apply {
            (group as? SourcesCategoryGroup)?.let {
                setSource(it.items[childIndex] as ArticleSource)
            }
        }
    }

    override fun onBindGroupViewHolder(holder: SourcesCategoryListItem?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.apply {
            (group as? SourcesCategoryGroup)?.let {
                setCategoryGroup(it)
            }
        }
    }
}