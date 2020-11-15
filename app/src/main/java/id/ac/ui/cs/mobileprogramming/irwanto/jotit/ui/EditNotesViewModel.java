package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class EditNotesViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private Note editableNote;

    public MutableLiveData<String> _titleTextField = new MutableLiveData<>();
    public MutableLiveData<String> _descTextField = new MutableLiveData<>();

    public EditNotesViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
    }

    public void saveNote(String noteId) {
        editableNote.title = (_titleTextField.getValue() == null || _titleTextField.getValue().isEmpty())
                ? "(untitled)" : _titleTextField.getValue();
        editableNote.description = _descTextField.getValue();
        if (noteId != null) {
            noteRepository.update(editableNote);
        } else {
            noteRepository.insert(editableNote);
        }
    }

    public void initNote(String noteId) {
        if (noteId != null) {
            editableNote = noteRepository.getNoteById(noteId);
        } else {
            editableNote = new Note();
        }
        _titleTextField.setValue(editableNote.title);
        _descTextField.setValue(editableNote.description);
    }
}