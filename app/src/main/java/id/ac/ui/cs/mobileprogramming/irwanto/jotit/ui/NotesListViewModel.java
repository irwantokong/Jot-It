package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class NotesListViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;

    private final LiveData<List<Note>> mAllNotes;

    public NotesListViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        mAllNotes = noteRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

}