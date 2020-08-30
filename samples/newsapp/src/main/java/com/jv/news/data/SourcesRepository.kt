package com.jv.news.data

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.joaquimverges.helium.ui.list.repository.ListRepository
import com.jv.news.App
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * @author joaquim
 */
class SourcesRepository(
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context.applicationContext),
    private val api: NewsApiServer.NewsApiService = NewsApiServer.service
) : ListRepository<List<SourcesCategoryGroup>> {

    companion object {
        private const val SELECTED_SOURCES = "selected_sources"
    }

    private val sources = mutableSetOf<String>().apply { addAll(preferences.getStringSet(SELECTED_SOURCES, mutableSetOf())?.toList() ?: listOf()) }
    private val sourcesSubject = BroadcastChannel<Set<String>>(Channel.BUFFERED)

    override suspend fun getFirstPage(): List<SourcesCategoryGroup> {
        return api.getSources().sources
            .groupBy { source -> source.category }
            .mapNotNull { mapEntry -> mapEntry.key?.let { name -> SourcesCategoryGroup(name, mapEntry.value) { source -> isSelected(source) } } }
    }

    fun getSelectedSourceIds(): MutableSet<String> {
        return sources
    }

    fun markUnselected(source: ArticleSource) {
        sources.apply {
            remove(source.id)
            sourcesSubject.offer(this)
        }
        persistToDisk()
    }

    fun markSelected(source: ArticleSource) {
        sources.apply {
            source.id?.let { add(it) }
            sourcesSubject.offer(this)
        }
        persistToDisk()
    }

    private fun persistToDisk() {
        preferences.edit().putStringSet(SELECTED_SOURCES, sources).apply()
    }

    private fun isSelected(articleSource: ArticleSource): Boolean {
        return getSelectedSourceIds().contains(articleSource.id)
    }

    fun observer(): Flow<Set<String>> = sourcesSubject.asFlow()
}
