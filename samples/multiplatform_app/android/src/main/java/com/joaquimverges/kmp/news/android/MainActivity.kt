package com.joaquimverges.kmp.news.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.joaquimverges.kmp.news.BrowserWrapper
import com.joaquimverges.kmp.news.logic.AppRouter
import com.joaquimvergse.helium.compose.AppBlock

class MainActivity : AppCompatActivity() {

    private lateinit var appRouter : AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appRouter = AppRouter(BrowserWrapper(this))
        setContent {
            AppUi(appRouter)
        }
    }

    override fun onBackPressed() {
        if (!appRouter.onBackPressed()) {
            super.onBackPressed()
        }
    }
}
