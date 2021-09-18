package com.example.notes.db
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
        @ColumnInfo(name = "title") val title: String?,
        @ColumnInfo(name ="datetime") val datetime: String?,
        @ColumnInfo(name = "subtitle") val subtitle: String?,
        @ColumnInfo(name = "notetext") val notetext: String?,
        @ColumnInfo(name = "imageUri") val imageUri: String?,
        @ColumnInfo(name= "color") val color: String?,
        @PrimaryKey(autoGenerate = true) var id: Int=0
) :Serializable
