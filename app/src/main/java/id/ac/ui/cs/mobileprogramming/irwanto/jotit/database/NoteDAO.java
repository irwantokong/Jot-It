package id.ac.ui.cs.mobileprogramming.irwanto.jotit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM notes_table ORDER BY _id DESC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes_table WHERE note_id = :noteId")
    LiveData<Note> getLivaDataNoteById(String noteId);

    @Query("SELECT * FROM notes_table WHERE note_id = :noteId")
    Note getNoteById(String noteId);

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}
