package com.varunbarad.toreadmanager.local_database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.varunbarad.toreadmanager.local_database.models.DbLink
import io.reactivex.Observable

@Dao
interface LinksDao {
    @Query("select * from Links order by id desc")
    fun getEntries(): Observable<List<DbLink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: DbLink)
}
