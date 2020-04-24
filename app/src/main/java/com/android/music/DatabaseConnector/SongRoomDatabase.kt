package com.android.music.DatabaseConnector

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.music.model.Songs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@Database(entities = [Songs::class], version = 1)
abstract class SongRoomDatabase : RoomDatabase() {

    abstract fun songDao(): SongDataAccessObject

    companion object {
        @Volatile
        private var INSTANCE: SongRoomDatabase? = null

        fun getDatabase(
            context: Context
        ): SongRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SongRoomDatabase::class.java,
                    "song_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomcallBack)
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class SongDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.songDao())
                    }
                }
            }
        }


        private class PopulateDbAsyncTask(db: SongRoomDatabase) :
            AsyncTask<Void, Void, Void>() {

            private val songDAO: SongDataAccessObject

            init {
                songDAO= db.songDao()
            }

            override fun doInBackground(vararg voids: Void): Void? {

                return null
            }
        }

        private val roomcallBack = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                PopulateDbAsyncTask(INSTANCE!!).execute()
                super.onCreate(db)
            }
        }



        fun populateDatabase(songDao: SongDataAccessObject) {
            var song = Songs("1","No Songs","https://pngimage.net/wp-content/uploads/2018/06/no-image-found-png-1-300x200.png","No ","No","1542")
            songDao.insert(song)
        }
    }

}