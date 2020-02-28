package com.joaquimverges.demoapp.data

import com.joaquimverges.demoapp.data.Colors.randomColor
import com.joaquimverges.helium.ui.list.repository.ListRepository
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyListRepository : ListRepository<List<MyItem>> {

    override fun getFirstPage(): Single<List<MyItem>> {
        return Flowable.range(0, 100)
                .map { i -> randomColor(i).run { MyItem(color, name.toLowerCase().replace("_", " ")) } }
                .toList()
                .delay(1, TimeUnit.SECONDS)
    }
}
