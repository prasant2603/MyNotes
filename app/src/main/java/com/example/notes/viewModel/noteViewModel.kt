package com.example.notes.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notes.db.Note
import com.example.notes.db.NoteDatabase
import com.example.notes.repository.NoteRepository
import kotlinx.coroutines.launch

class noteViewModel(application: Application) :AndroidViewModel(application)
{
    private lateinit var repository: NoteRepository
    val allNotes : LiveData<List<Note>>
    init
    {
        val dao = NoteDatabase.getDatabase(application).NoteDao()
        repository = NoteRepository(dao)
        allNotes=repository.allNotes
    }
    fun addNote(note: Note) = viewModelScope.launch {
        repository.addNote(note)
    }
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
    fun deleteAll()=viewModelScope.launch {
        repository.deleteAll()
    }
}