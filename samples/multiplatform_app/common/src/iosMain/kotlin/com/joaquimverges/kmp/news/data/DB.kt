package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.HeliumNewsDB
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun createDatabase(): HeliumNewsDB {
    val driver = NativeSqliteDriver(HeliumNewsDB.Schema, "helium_news.db")
    return HeliumNewsDB(driver)
}