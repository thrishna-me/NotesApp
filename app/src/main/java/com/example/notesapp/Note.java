package com.example.notesapp;

import com.google.firebase.database.Exclude;

public class Note {
    @Exclude
    String noteId;

    @Exclude
    public String getNoteId() {
        return noteId;
    }

    @Exclude
    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }



    String noteTitle, noteDescription;

    public Note() {
        //This is required for firebase
    }

    public Note(String noteTitle, String noteDescription) {
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }
}
