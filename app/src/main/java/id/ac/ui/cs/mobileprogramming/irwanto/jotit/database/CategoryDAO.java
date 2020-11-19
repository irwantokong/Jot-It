package id.ac.ui.cs.mobileprogramming.irwanto.jotit.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;

@Dao
public interface CategoryDAO {
    @Query("SELECT * FROM categories_table")
    LiveData<List<Category>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertCategory(Category category);

    @Delete
    void deleteCategory(Category category);
}
