package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class DisplayNoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    public LiveData<Note> viewedNote;

    public DisplayNoteViewModel(Application application) {
        super(application);
        this.noteRepository = new NoteRepository(application);
    }

    public void setNote(String noteId) {
        viewedNote = noteRepository.getLiveDataNoteById(noteId);
    }
}