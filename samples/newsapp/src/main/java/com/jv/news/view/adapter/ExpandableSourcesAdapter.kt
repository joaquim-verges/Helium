package com.jv.news.view.adapter

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
class ExpandableSourcesAdapter(private val context: Context, categoryGroups: List<SourcesCategoryGroup> = mutableListOf())
    : CheckableChildRecyclerViewAdapter<SourcesCategoryGroupViewHolder, SourceViewHolder>(categoryGroups) {

    override fun onCreateCheckChildViewHolder(parent: ViewGroup?, viewType: Int): SourceViewHolder = SourceViewHolder.create(context, parent)

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int) = SourcesCategoryGroupViewHolder.create(context, parent)

    override fun onBindCheckChildViewHolder(holder: SourceViewHolder?, flatPosition: Int, group: CheckedExpandableGroup?, childIndex: Int) {
        holder?.apply {
            (group as? SourcesCategoryGroup)?.let {
                setSource(it.items[childIndex] as ArticleSource)
            }
        }
    }

    override fun onBindGroupViewHolder(holder: SourcesCategoryGroupViewHolder?, flatPosition: Int, group: ExpandableGroup<*>?) {
        holder?.apply {
            (group as? SourcesCategoryGroup)?.let {
                setCategoryGroup(it)
            }
        }
    }
}