package com.android.music.DatabaseConnector

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.music.model.Songs

@Dao
interface SongDataAccessObject {
@Query("Select * from songs_table")
fun getAllSongs(): LiveData<List<Songs>>

    @Insert
    fun insert(songs: Songs)

    @Query("DELETE FROM songs_table")
    fun deleteAll()

   }