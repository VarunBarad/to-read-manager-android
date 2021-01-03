package com.varunbarad.toreadmanager.export

import com.squareup.moshi.Moshi
import com.varunbarad.toreadmanager.di.AppScope
import dagger.Module
import dagger.Provides

@Module
object ExportModule {
    @Provides
    @AppScope
    fun moshi(): Moshi {
        return Moshi.Builder()
            .build()
    }
}
