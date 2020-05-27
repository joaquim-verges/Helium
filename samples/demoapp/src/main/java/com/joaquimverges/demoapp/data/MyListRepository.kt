package com.joaquimverges.demoapp.data

import com.joaquimverges.demoapp.data.Colors.randomColor
import com.joaquimverges.helium.ui.list.repository.ListRepository
import kotlinx.coroutines.delay

/**
 * @author joaquim
 */
class MyListRepository : ListRepository<List<MyItem>> {

    override suspend fun getFirstPage(): List<MyItem> {
        delay(1000)
        return (0..100).toList()
            .map { randomColor(it).run { MyItem(color, name.toLowerCase().replace("_", " ")) } }
    }
}
