package com.joaquimverges.kmp.news.android

import android.app.Application
import com.joaquimverges.kmp.news.BrowserWrapper
import com.joaquimverges.kmp.news.logic.AppRouter

class App : Application() {

    companion object {
        lateinit var router: AppRouter
            private set
    }

    override fun onCreate() {
        super.onCreate()
        router = AppRouter(BrowserWrapper(this))
    }
}
