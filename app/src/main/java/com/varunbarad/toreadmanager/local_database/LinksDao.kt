package com.varunbarad.toreadmanager.local_database

import androidx.room.*
import com.varunbarad.toreadmanager.local_database.models.DbLink
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface LinksDao {
    @Query("select * from Links where archived is 0 order by id desc")
    fun getActiveEntries(): Observable<List<DbLink>>

    @Query("select * from Links where archived is 1 order by id desc")
    fun getArchivedEntries(): Observable<List<DbLink>>

    @Query("select * from Links order by id desc")
    fun getAllEntries(): Observable<List<DbLink>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: DbLink): Completable

    @Delete
    fun deleteEntry(entry: DbLink): Completable

    @Update
    fun updateEntry(entry: DbLink): Completable
}
