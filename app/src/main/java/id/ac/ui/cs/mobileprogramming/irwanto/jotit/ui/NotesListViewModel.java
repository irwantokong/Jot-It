package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.CategoryRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class NotesListViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;

    private final MutableLiveData<List<Note>> allNotes = new MutableLiveData<>();
    private final LiveData<List<Category>> allCategories;

    public NotesListViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        categoryRepository = new CategoryRepository(application);
        allNotes.setValue(noteRepository.getAllNotes());
        allCategories = categoryRepository.getAllCategories();
    }

    public MutableLiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void filterNotesByCategory(Category category, boolean all) {
        allNotes.setValue(all ? noteRepository.getAllNotes() : noteRepository.getNotesOfCategoryId(category.id));
    }

    public void addNewCategory(String categoryName) {
        Category category = new Category();
        category.name = categoryName;
        categoryRepository.insert(category);
    }

    public void removeCategory(Category category) {
        categoryRepository.delete(category);
    }
}