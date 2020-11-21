package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.CategoryRepository;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository.NoteRepository;

public class NotesListViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;

    private final LiveData<List<Note>> allNotesFilteredByCategory;
    private final LiveData<List<Category>> allCategories;
    private final MutableLiveData<Category> category = new MutableLiveData<>();

    public NotesListViewModel(Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
        allNotesFilteredByCategory = Transformations.switchMap(category, c -> {
            if (c.name.equals(application.getString(R.string.all_categories).toUpperCase())) {
                return noteRepository.getAllNotes();
            }
            return noteRepository.getNotesOfCategoryId(c.id);
        });
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public LiveData<List<Note>> getAllNotesFilteredByCategory() {
        return allNotesFilteredByCategory;
    }

    public void setCategory(Category category) {
        this.category.setValue(category);
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