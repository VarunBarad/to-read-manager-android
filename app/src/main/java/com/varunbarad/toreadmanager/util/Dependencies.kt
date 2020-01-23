package com.varunbarad.toreadmanager.util

import android.content.Context
import androidx.room.Room
import com.varunbarad.toreadmanager.local_database.LinksDatabase

object Dependencies {
    private lateinit var toReadDatabase: LinksDatabase

    fun getToReadDatabase(context: Context): LinksDatabase {
        synchronized(this) {
            if (!this::toReadDatabase.isInitialized) {
                this.toReadDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    LinksDatabase::class.java,
                    LinksDatabase.databaseName
                ).build()
            }
        }

        return this.toReadDatabase
    }
}
