package id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;


public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context) {
        super(context, android.R.layout.simple_spinner_item);
        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
