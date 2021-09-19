package com.example.notes.repository

import androidx.lifecycle.LiveData
import com.example.notes.db.Note
import com.example.notes.db.NoteDao

class NoteRepository(private val noteDao: NoteDao)  {
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    fun searchDatabase(searchQuery: String): LiveData<List<Note>>
    {
        return noteDao.searchDatabase(searchQuery)
    }
    suspend fun delete(note: Note)
    {
        noteDao.delete(note)
    }
    suspend fun addNote(note: Note)
    {
        noteDao.addNote(note)
    }
    suspend fun deleteAll()
    {
        noteDao.deleteAll()
    }
}