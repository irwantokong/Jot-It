package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.CategoryRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class EditNotesViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    private Note editableNote;

    public LiveData<Note> loadedNote;
    public LiveData<List<Category>> allCategories;
    public MutableLiveData<String> titleTextField = new MutableLiveData<>();
    public MutableLiveData<String> descTextField = new MutableLiveData<>();

    public EditNotesViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        CategoryRepository categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public void saveNote(boolean isEdit) {
        editableNote.title = (titleTextField.getValue() == null || titleTextField.getValue().isEmpty())
                ? "(untitled)" : titleTextField.getValue();
        editableNote.description = descTextField.getValue();

        if (isEdit) {
            noteRepository.update(editableNote);
        } else {
            noteRepository.insert(editableNote);
        }
    }

    public void initNote(String noteId, boolean isEdit) {
        if (isEdit) {
            loadedNote = noteRepository.getLiveDataNoteById(noteId);
        } else {
            editableNote = new Note();
            updateUI();
        }
    }

    public void setEditableNote(Note note) {
        editableNote = note;
        updateUI();
    }

    public Note getEditableNote() {
        return editableNote;
    }

    private void updateUI() {
        titleTextField.setValue(editableNote.title);
        descTextField.setValue(editableNote.description);
    }

    public void setImageFilePath(String path) {
        editableNote.imagePath = path;
    }

    public String getImageFilePath() {
        return editableNote.imagePath;
    }

    public String getNoteImageFilename() {
        return editableNote.getImageFilename();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void setCategory(Category category) {
        editableNote.category = category;
    }

    public LiveData<Note> getLoadedNote() {
        LiveData<Note> noteLiveData = loadedNote;
        LiveData<List<Category>> categoryListLiveData = allCategories;

        MediatorLiveData<Note> result = new MediatorLiveData<>();
        result.addSource(noteLiveData, new Observer<Note>() {
            @Override
            public void onChanged(Note note) {
                result.setValue(combineLiveData(noteLiveData, categoryListLiveData));
            }
        });
        result.addSource(categoryListLiveData, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                result.setValue(combineLiveData(noteLiveData, categoryListLiveData));
            }
        });

        return result;
    }

    private Note combineLiveData(LiveData<Note> noteLiveData, LiveData<List<Category>> categoryListLiveData) {
        if (noteLiveData.getValue() == null || categoryListLiveData.getValue() == null) {
            return null;
        }
        return noteLiveData.getValue();
    }
}