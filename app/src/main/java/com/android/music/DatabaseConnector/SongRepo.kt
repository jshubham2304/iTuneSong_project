package com.android.music.DatabaseConnector

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.android.music.model.Songs


class SongRepo(application: Application) {
    lateinit var allSongs: LiveData<List<Songs>>
    lateinit var songsDao:SongDataAccessObject


    init {
        val songdatabase=SongRoomDatabase.getDatabase(application)
        songsDao = songdatabase.songDao()
        allSongs=songsDao.getAllSongs()

    }
    fun insert(song: Songs) {
        songsDao.insert(song)
    }

    fun deleteAll() {
        deleteAllWordsAsyncTask(songsDao).execute()
    }
    private class deleteAllWordsAsyncTask internal constructor(dao: SongDataAccessObject) :
        AsyncTask<Void?, Void?, Void?>() {
        private val mAsyncTaskDao: SongDataAccessObject
        override fun doInBackground(vararg voids: Void?): Void? {
            mAsyncTaskDao.deleteAll()
            return null
        }

        init {
            mAsyncTaskDao = dao
        }
    }
}
