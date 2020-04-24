package com.android.music.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "songs_table")
data class Songs (@PrimaryKey @ColumnInfo(name = "trackId") val trackId : String ,val trackName : String, val image : String, val collectionName : String, val primaryGenreName : String, val trackTimeMillis: String)
