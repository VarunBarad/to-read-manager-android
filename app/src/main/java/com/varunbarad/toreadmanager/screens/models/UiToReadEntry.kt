package com.varunbarad.toreadmanager.screens.models

import androidx.recyclerview.widget.DiffUtil

data class UiToReadEntry(
    val databaseId: Int?,
    val url: String,
    val title: String,
    val archived: Boolean
) {
    companion object {
        @JvmStatic
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UiToReadEntry>() {
            override fun areItemsTheSame(oldItem: UiToReadEntry, newItem: UiToReadEntry): Boolean {
                return (oldItem.databaseId == newItem.databaseId)
            }

            override fun areContentsTheSame(
                oldItem: UiToReadEntry,
                newItem: UiToReadEntry
            ): Boolean {
                return (oldItem == newItem)
            }
        }
    }
}
