package com.joaquimverges.demoapp.data

import com.joaquimverges.demoapp.data.Colors.randomColor
import com.joaquimverges.helium.repository.BaseRepository
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyListRepository : BaseRepository<List<MyItem>> {

    override fun getData(): Single<List<MyItem>> {
        return Flowable.range(0, 100)
                .map { i -> MyItem(randomColor(i)) }
                .toList()
                .delay(1, TimeUnit.SECONDS)
    }
}
