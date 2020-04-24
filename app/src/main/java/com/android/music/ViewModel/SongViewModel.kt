package com.android.music.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.music.DatabaseConnector.SongRepo
import com.android.music.model.Songs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public class SongViewModel(application: Application): AndroidViewModel(application) {
    private val repo: SongRepo
    val allSongs:LiveData<List<Songs>>

    init {
        repo = SongRepo(application)
        allSongs = repo.allSongs
    }

    fun insert(song: Songs) = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(song)
    }

    fun deleteAll(){
       repo.deleteAll()
   }

}