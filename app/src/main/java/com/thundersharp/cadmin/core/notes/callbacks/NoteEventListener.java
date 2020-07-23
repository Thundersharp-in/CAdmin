package com.thundersharp.cadmin.core.notes.callbacks;


import com.thundersharp.cadmin.core.notes.model.Note;


public interface NoteEventListener {
    /**
     * call wen note clicked.
     *
     * @param note: note item
     */
    void onNoteClick(Note note);

    /**
     * call wen long Click to note.
     *
     * @param note : item
     */
    void onNoteLongClick(Note note);
}
