package com.jv.news.data.model

import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup

/**
 * @author joaquim
 */
class SourcesCategoryGroup(
    name: String,
    private val sources: List<ArticleSource>,
    private val childCheckedFunction: (source: ArticleSource) -> Boolean
) : MultiCheckExpandableGroup(name, sources) {

    override fun isChildChecked(childIndex: Int): Boolean {
        return childCheckedFunction.invoke(sources[childIndex])
    }
}
