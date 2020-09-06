package com.joaquimverges.kmp.news

import android.content.Context
import android.content.Intent
import android.net.Uri

actual class BrowserWrapper(
    private val context: Context
) {
    actual fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }
}