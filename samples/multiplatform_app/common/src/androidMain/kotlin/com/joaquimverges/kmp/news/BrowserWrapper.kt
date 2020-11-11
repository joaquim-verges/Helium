package com.joaquimverges.kmp.news

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri

actual class BrowserWrapper(
    private val context: Context
) {
    actual fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}
