package com.joaquimverges.kmp.news

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class BrowserWrapper {
    actual fun openBrowser(url: String) {
        NSURL.URLWithString(url)?.let {
            UIApplication.sharedApplication.openURL(it)
        }
    }
}
