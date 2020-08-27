package com.joaquimverges.kmp.news.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joaquimverges.helium.core.assemble
import com.joaquimverges.helium.core.plus
import com.joaquimverges.kmp.news.CommonListLogic

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ui = ArticlesListUi(this)
        assemble(CommonListLogic() + ui)
        setContentView(ui.view)
    }

}