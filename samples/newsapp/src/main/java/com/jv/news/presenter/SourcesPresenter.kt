package com.jv.news.presenter

import com.joaquimverges.helium.presenter.DataListPresenter
import com.joaquimverges.helium.util.RefreshPolicy
import com.jv.news.data.SourcesRepository
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.view.event.SourceEvent
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class SourcesPresenter(private val sourcesRepository: SourcesRepository = SourcesRepository(),
                       refreshPolicy: RefreshPolicy = RefreshPolicy(1, TimeUnit.HOURS))
    : DataListPresenter<SourcesCategoryGroup, SourceEvent>(sourcesRepository, refreshPolicy) {

    override fun onViewEvent(event: SourceEvent) {
        when (event) {
            is SourceEvent.Selected -> sourcesRepository.markSelected(event.source)
            is SourceEvent.Unselected -> sourcesRepository.markUnselected(event.source)
        }
    }
}