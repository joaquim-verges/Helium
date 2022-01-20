package com.jv.news

import android.app.Application

/**
 * @author joaquim
 */
class App : Application() {

    companion object {
        lateinit var context: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
