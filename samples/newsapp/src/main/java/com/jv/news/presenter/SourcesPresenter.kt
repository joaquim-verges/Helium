package com.jv.news.presenter

import com.joaquimverges.helium.ui.event.ListViewEvent
import com.joaquimverges.helium.ui.presenter.ListPresenter
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.SourcesRepository
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.view.event.SourceEvent
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class SourcesPresenter(
    private val sourcesRepository: SourcesRepository = SourcesRepository(),
    refreshPolicy: RefreshPolicy = RefreshPolicy(1, TimeUnit.HOURS)
) : ListPresenter<SourcesCategoryGroup, SourceEvent>(sourcesRepository, refreshPolicy) {

    override fun onViewEvent(event: ListViewEvent<SourceEvent>) {
        when (event) {
            is ListViewEvent.ListItemEvent -> {
                when (val itemEvent = event.itemEvent) {
                    is SourceEvent.Selected -> sourcesRepository.markSelected(itemEvent.source)
                    is SourceEvent.Unselected -> sourcesRepository.markUnselected(itemEvent.source)
                }
            }
        }
    }
}