package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class EditNotesViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;

    public EditNotesViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public void saveNote(Note note) {
        noteRepository.insert(note);
    }

    public Note initNote() {
        Note editableNote = new Note();
        return editableNote;
    }
}