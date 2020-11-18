package com.joaquimverges.kmp.news.data

import android.content.Context
import com.joaquimverges.kmp.news.HeliumNewsDB
import com.squareup.sqldelight.android.AndroidSqliteDriver

lateinit var appContext: Context

actual fun createDatabase(): HeliumNewsDB {
    val driver = AndroidSqliteDriver(HeliumNewsDB.Schema, appContext, "helium_news.db")
    return HeliumNewsDB(driver)
}