@file:JvmName("StringExtensions")

package com.varunbarad.toreadmanager.util

/**
 * This extracts the first http:// or https:// urls if any exist
 *
 * @return First http:// or https:// url or `null` if no url is present
 */
fun String.extractUrlIfAny(): String? {
    return Regex(
        pattern = "(http://.+)|(https://.+)",
        options = setOf(RegexOption.IGNORE_CASE),
    ).find(this)?.value
}

fun String.isUrl(): Boolean {
    return Regex(
        pattern = "(http://.+)|(https://.+)",
        options = setOf(RegexOption.IGNORE_CASE),
    ).matches(this)
}
