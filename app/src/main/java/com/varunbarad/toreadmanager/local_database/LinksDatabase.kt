package com.varunbarad.toreadmanager.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.varunbarad.toreadmanager.local_database.models.DbLink

@Database(
    entities = [
        DbLink::class
    ],
    version = LinksDatabase.databaseVersion,
    exportSchema = false,
)
abstract class LinksDatabase : RoomDatabase() {
    abstract fun linksDao(): LinksDao

    companion object {
        const val databaseVersion = 1
        const val databaseName = "To-ReadDatabase"

        fun getInstance(context: Context): LinksDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                LinksDatabase::class.java,
                databaseName,
            ).build()
        }
    }
}
