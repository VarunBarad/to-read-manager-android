package com.varunbarad.toreadmanager.export.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ExportLink(
    @Json(name = "id") val id: Long,
    @Json(name = "title") val title: String,
    @Json(name = "url") val url: String,
    @Json(name = "archived") val archived: Boolean,
)
