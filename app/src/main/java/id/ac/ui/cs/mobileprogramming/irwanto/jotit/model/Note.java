package id.ac.ui.cs.mobileprogramming.irwanto.jotit.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "notes_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "note_id")
    public String noteId = UUID.randomUUID().toString();

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "image_path")
    public String imagePath;

    public String getImageFilename() {
        return "IMG_" + noteId + ".jpg";
    }
}
