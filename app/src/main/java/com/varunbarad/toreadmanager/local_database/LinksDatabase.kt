package com.varunbarad.toreadmanager.local_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.varunbarad.toreadmanager.local_database.models.DbLink

@Database(
    entities = [
        DbLink::class
    ],
    version = LinksDatabase.databaseVersion
)
abstract class LinksDatabase : RoomDatabase() {
    abstract fun linksDao(): LinksDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "To-ReadDatabase"
    }
}
