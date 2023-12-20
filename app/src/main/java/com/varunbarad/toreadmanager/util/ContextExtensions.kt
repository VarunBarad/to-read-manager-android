@file:JvmName("ContextExtensions")

package com.varunbarad.toreadmanager.util

import android.content.Context
import android.net.Uri
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.varunbarad.toreadmanager.R

fun Context.openLinkInCustomTab(link: String) {
    @ColorInt val toolbarColor = ContextCompat.getColor(this, R.color.colorPrimary)
    val colorScheme = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(toolbarColor)
        .build()

    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(colorScheme)
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(this, Uri.parse(link))
}
