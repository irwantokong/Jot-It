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

    public List<Note> getAllNotes() {
        Future<List<Note>> future = AppDatabase.databaseWriteExecutor.submit(new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                return noteDAO.getAllNotes();
            }
        });

        try {
            return (List<Note>) future.get();
        } catch (ExecutionException | InterruptedException e){
            Log.e("NoteRepository", e.toString());
        }

        return null;
    }

    public LiveData<Note> getLiveDataNoteById(String noteId) {
        return noteDAO.getLivaDataNoteById(noteId);
    }

    public List<Note> getNotesOfCategoryId(int categoryId) {
        Future<List<Note>> future = AppDatabase.databaseWriteExecutor.submit(new Callable<List<Note>>() {
            @Override
            public List<Note> call() throws Exception {
                return noteDAO.getNotesOfCategory(categoryId);
            }
        });

        try {
            return future.get();
        } catch (ExecutionException | InterruptedException e){
            Log.e("NoteRepository", e.toString());
        }
        return null;
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
