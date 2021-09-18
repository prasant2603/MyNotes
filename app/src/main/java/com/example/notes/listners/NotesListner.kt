package com.example.notes.listners

import com.example.notes.db.Note

interface NotesListner {
    fun onNoteClicked(note: Note, position: Int)
}