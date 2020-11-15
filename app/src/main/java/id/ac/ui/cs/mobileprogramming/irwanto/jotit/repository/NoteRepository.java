package id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
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

    public Note getNoteById(String noteId) {
        final Note[] note = new Note[1];
        try {
            AppDatabase.databaseWriteExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    note[0] = noteDAO.getNoteById(noteId);
                }
            }).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return note[0];
    }

    public void insert(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDAO.insertNote(note);
        });
    }

    public void update(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDAO.updateNote(note);
        });
    }

    public void delete(Note note) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            noteDAO.deleteNote(note);
        });
    }
}
