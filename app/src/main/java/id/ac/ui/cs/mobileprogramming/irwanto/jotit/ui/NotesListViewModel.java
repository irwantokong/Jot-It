package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.CategoryRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class NotesListViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private CategoryRepository categoryRepository;

    private MutableLiveData<List<Note>> mAllNotes = new MutableLiveData<>();
    private LiveData<List<Category>> mAllCategories;

    public NotesListViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        categoryRepository = new CategoryRepository(application);
        mAllNotes.setValue(noteRepository.getAllNotes());
        mAllCategories = categoryRepository.getAllCategories();
    }

    public MutableLiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public void filterNotesByCategory(Category category, boolean all) {
        mAllNotes.setValue(all ? noteRepository.getAllNotes() : noteRepository.getNotesOfCategoryId(category.id));
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