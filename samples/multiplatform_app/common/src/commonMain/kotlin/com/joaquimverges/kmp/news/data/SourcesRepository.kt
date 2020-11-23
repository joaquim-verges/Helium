package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.Sources
import com.joaquimverges.kmp.news.data.models.ArticleSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SourcesRepository(private val api: NewsApi = NewsApi(), private val db: Database = Database) {

    suspend fun refreshSources() {
        val sources = api.getSources().sources
        sources.forEach {
            val selected = db.sources()
                .getSourceById(it.id ?: "")
                .executeAsOneOrNull()?.selected ?: false
            db.sources().insertSource(
                Sources(it.id ?: "", it.name ?: "", it.category ?: "", selected)
            )
        }
    }

    fun observeSources() =
        db.sources().getAllSources().asFlow().mapToList()

    fun observeSelectedSources() =
        db.sources().getSelectedSources().asFlow().mapToList()

    fun getSelectedSourceIds(): List<String> {
        return db.sources().getSelectedSources().executeAsList().map { it.id }
    }

    fun setSelectedSource(source: Sources, selected: Boolean) {
        db.sources().setSourceSelected(selected, source.id)
    }
}
