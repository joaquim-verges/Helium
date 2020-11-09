package com.joaquimverges.kmp.news.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent

class MainActivity : AppCompatActivity() {

    private val appRouter = App.router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppUi(appRouter)
        }
    }

    override fun onBackPressed() {
        if (!appRouter.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun finish() {
        resetScrollPosition()
        super.finish()
    }
}
