package com.varunbarad.toreadmanager.util

import com.varunbarad.toreadmanager.local_database.models.DbLink
import com.varunbarad.toreadmanager.screens.models.UiToReadEntry

fun DbLink.toUiToReadEntry(): UiToReadEntry {
    return UiToReadEntry(
        databaseId = this.id,
        url = this.url,
        title = this.title,
        archived = this.archived
    )
}

fun UiToReadEntry.toDbLink(): DbLink {
    return DbLink(
        id = this.databaseId,
        url = this.url,
        title = this.title,
        archived = this.archived
    )
}
