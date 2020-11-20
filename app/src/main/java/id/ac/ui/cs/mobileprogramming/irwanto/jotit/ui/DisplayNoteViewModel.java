package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class DisplayNoteViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    public LiveData<Note> viewedNote;

    public DisplayNoteViewModel(Application application) {
        super(application);
        this.noteRepository = new NoteRepository(application);
    }

    public void setNote(String noteId) {
        viewedNote = noteRepository.getLiveDataNoteById(noteId);
    }

    public void deleteNote() {
        noteRepository.delete(viewedNote.getValue());
    }

    public LiveData<Note> getViewedNote() {
        return viewedNote;
    }
}