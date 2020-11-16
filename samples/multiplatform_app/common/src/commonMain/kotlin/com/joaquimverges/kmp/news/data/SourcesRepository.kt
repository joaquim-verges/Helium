package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.data.models.ArticleSource

data class SourceWithSelection(val sources: List<ArticleSource>, val selectedMap: Map<String, Boolean>)

class SourcesRepository(private val api: NewsApi = NewsApi()) {

    private val selectedSources = mutableMapOf<String, Boolean>() // TODO local storage

    suspend fun getSources(): SourceWithSelection {
        return SourceWithSelection(api.getSources().sources, getSelectedSourcesMap())
    }

    fun getSelectedSourcesMap(): Map<String, Boolean> {
        return selectedSources
    }

    fun setSelectedSource(source: ArticleSource, selected: Boolean) {
        source.id?.let {
            selectedSources[source.id] = selected
        }
    }
}
