package com.jv.news.data

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.joaquimverges.helium.ui.repository.BaseRepository
import com.jv.news.App
import com.jv.news.data.model.ArticleSource
import com.jv.news.data.model.SourcesCategoryGroup
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

/**
 * @author joaquim
 */
class SourcesRepository(
    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.context.applicationContext),
    private val api: NewsApiServer.NewsApiService = NewsApiServer.service
) : BaseRepository<List<SourcesCategoryGroup>> {

    companion object {
        private const val SELECTED_SOURCES = "selected_sources"
    }

    private val sources = mutableSetOf<String>().apply { addAll(preferences.getStringSet(SELECTED_SOURCES, mutableSetOf())?.toList() ?: listOf()) }
    private val sourcesSubject = PublishSubject.create<Set<String>>()

    override fun getData(): Single<List<SourcesCategoryGroup>> {
        return api.getSources()
            .map { it.sources.groupBy { it.category } }
            .map { it.mapNotNull { mapEntry -> mapEntry.key?.let { SourcesCategoryGroup(it, mapEntry.value) { source -> isSelected(source) } } } }
    }

    fun getSelectedSourceIds(): MutableSet<String> {
        return sources
    }

    fun markUnselected(source: ArticleSource) {
        sources.apply {
            remove(source.id)
            sourcesSubject.onNext(this)
        }
        persistToDisk()
    }

    fun markSelected(source: ArticleSource) {
        sources.apply {
            source.id?.let { add(it) }
            sourcesSubject.onNext(this)
        }
        persistToDisk()
    }

    private fun persistToDisk() {
        preferences.edit().putStringSet(SELECTED_SOURCES, sources).apply()
    }

    private fun isSelected(articleSource: ArticleSource): Boolean {
        return getSelectedSourceIds().contains(articleSource.id)
    }

    fun observer(): Observable<Set<String>> = sourcesSubject
}