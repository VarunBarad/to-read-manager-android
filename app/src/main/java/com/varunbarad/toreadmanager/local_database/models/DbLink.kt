package com.varunbarad.toreadmanager.local_database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Links",
    indices = [
        Index(value = ["url"], unique = true)
    ]
)
data class DbLink(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "archived") val archived: Boolean
)
