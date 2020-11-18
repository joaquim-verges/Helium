package com.joaquimverges.kmp.news.data

import com.joaquimverges.kmp.news.HeliumNewsDB

expect fun createDatabase(): HeliumNewsDB

object Database {
    private val db by lazy { createDatabase() }
    fun sources() = db.sourcesQueries
}
