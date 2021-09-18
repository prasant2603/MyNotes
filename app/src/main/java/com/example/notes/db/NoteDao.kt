package com.example.notes.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM notes order by id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAll()
}