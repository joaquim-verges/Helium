package com.jv.news.logic

import com.joaquimverges.helium.ui.list.event.ListBlockEvent
import com.joaquimverges.helium.ui.list.ListLogic
import com.joaquimverges.helium.ui.util.RefreshPolicy
import com.jv.news.data.SourcesRepository
import com.jv.news.data.model.SourcesCategoryGroup
import com.jv.news.ui.event.SourceEvent
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class SourcesLogic(
    private val sourcesRepository: SourcesRepository = SourcesRepository(),
    refreshPolicy: RefreshPolicy = RefreshPolicy(1, TimeUnit.HOURS)
) : ListLogic<SourcesCategoryGroup, SourceEvent>(sourcesRepository, refreshPolicy) {

    override fun onUiEvent(event: ListBlockEvent<SourceEvent>) {
        when (event) {
            is ListBlockEvent.ListItemEvent -> {
                when (val itemEvent = event.itemEvent) {
                    is SourceEvent.Selected -> sourcesRepository.markSelected(itemEvent.source)
                    is SourceEvent.Unselected -> sourcesRepository.markUnselected(itemEvent.source)
                }
            }
        }
    }
}