@file:JvmName("ContextExtensions")

package com.varunbarad.toreadmanager.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openLinkInBrowser(link: String) {
    this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
}
