package com.thundersharp.cadmin.notes.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.thundersharp.cadmin.notes.model.Note;

import java.util.List;

/**
 * Notes Data Object access help to access the notes
 * Created by ixi.Dv on 20/06/2018.
 */
@Dao
public interface NotesDao {
    /**
     * Insert and save note to Database
     *
     * @param note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    /**
     * Delete note
     *
     * @param note that will be delete
     */
    @Delete
    void deleteNote(Note... note);

    /**
     * Update note
     *
     * @param note the note that will be update
     */
    @Update
    void updateNote(Note note);

    /**
     * List All Notes From Database
     *
     * @return list of Notes
     */
    @Query("SELECT * FROM notes")
    List<Note> getNotes();

    /**
     * @param noteId note id
     * @return Note
     */
    @Query("SELECT * FROM notes WHERE id = :noteId")
    Note getNoteById(int noteId);

    /**
     * Delete Note by Id from DataBase
     *
     * @param noteId
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteNoteById(int noteId);

}
