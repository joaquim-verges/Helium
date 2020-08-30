package com.joaquimverges.kmp.news.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.joaquimverges.kmp.news.logic.AppRouter

class MainActivity : AppCompatActivity() {

    private val appRouter = AppRouter.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBlock(appRouter) { state, _ ->
                AppUi(state)
            }
        }
    }

    override fun onBackPressed() {
        if (!appRouter.onBackPressed()) {
            super.onBackPressed()
        }
    }
}