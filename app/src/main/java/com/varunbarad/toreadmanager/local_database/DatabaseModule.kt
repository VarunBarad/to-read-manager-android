package com.varunbarad.toreadmanager.local_database

import android.app.Application
import androidx.room.Room
import com.varunbarad.toreadmanager.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object DatabaseModule {
    @Provides
    @AppScope
    fun getLinksDatabase(context: Application): LinksDatabase {
        return Room.databaseBuilder(
            context,
            LinksDatabase::class.java,
            LinksDatabase.databaseName
        ).build()
    }

    @Provides
    fun linksDao(linksDatabase: LinksDatabase): LinksDao {
        return linksDatabase.linksDao()
    }
}
