package id.ac.ui.cs.mobileprogramming.irwanto.jotit.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.AppDatabase;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.database.CategoryDAO;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;

public class CategoryRepository {
    private CategoryDAO categoryDAO;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        categoryDAO = db.categoryDAO();
    }

    public LiveData<List<Category>> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public void insert(Category category) {
        AppDatabase.databaseExecutor.execute(() -> {
            categoryDAO.insertCategory(category);
        });
    }

    public void delete(Category category) {
        AppDatabase.databaseExecutor.execute(() -> {
            categoryDAO.deleteCategory(category);
        });
    }
}
