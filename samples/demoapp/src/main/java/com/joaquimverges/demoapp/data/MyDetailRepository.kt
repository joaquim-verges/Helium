package com.joaquimverges.demoapp.data

import com.joaquimverges.demoapp.data.Colors.randomColor
import com.joaquimverges.helium.ui.repository.BaseRepository
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author joaquim
 */
class MyDetailRepository : BaseRepository<MyItem> {

    override fun getData(): Single<MyItem> {
        return Single
            .just(randomColor(Random().nextInt()).run { MyItem(color, name.toLowerCase().replace("_", " ")) })
            .delay(1, TimeUnit.SECONDS)
    }
}
