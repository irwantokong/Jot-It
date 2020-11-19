package id.ac.ui.cs.mobileprogramming.irwanto.jotit.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories_table", indices = {@Index(value = {"name"}, unique = true)})
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
