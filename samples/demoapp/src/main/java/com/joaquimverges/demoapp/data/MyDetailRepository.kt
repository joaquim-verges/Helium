package com.joaquimverges.demoapp.data

import com.joaquimverges.demoapp.data.Colors.randomColor
import kotlinx.coroutines.delay
import java.util.Random

/**
 * @author joaquim
 */
class MyDetailRepository {

    suspend fun getData(): MyItem {
        delay(1000)
        return randomColor(Random().nextInt()).run { MyItem(color, name.toLowerCase().replace("_", " ")) }
    }
}
