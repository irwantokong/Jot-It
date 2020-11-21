package id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.NoteDAO;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;

public class NoteRepository {
    private NoteDAO noteDAO;

    public NoteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        noteDAO = db.noteDAO();
    }

    public LiveData<List<Note>> getAllNotes() {
        return noteDAO.getAllNotes();
    }

    public LiveData<Note> getLiveDataNoteById(String noteId) {
        return noteDAO.getLivaDataNoteById(noteId);
    }

    public LiveData<List<Note>> getNotesOfCategoryId(int categoryId) {
        return noteDAO.getNotesOfCategory(categoryId);
    }

    public void insert(Note note) {
        AppDatabase.databaseExecutor.execute(() -> {
            noteDAO.insertNote(note);
        });
    }

    public void update(Note note) {
        AppDatabase.databaseExecutor.execute(() -> {
            noteDAO.updateNote(note);
        });
    }

    public void delete(Note note) {
        AppDatabase.databaseExecutor.execute(() -> {
            noteDAO.deleteNote(note);
        });
    }
}
