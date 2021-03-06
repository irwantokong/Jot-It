package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter.CategoryAdapter;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter.NotesListAdapter;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;

import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.LEFT_CONTAINER_TAG;
import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.RIGHT_CONTAINER_TAG;

public class NotesListFragment extends Fragment implements NotesListAdapter.ListItemOnClickListener {

    private FragmentManager fragmentManager;
    private NotesListViewModel mViewModel;
    private NotesListAdapter adapter;
    private CategoryAdapter categoryAdapter;
    private int orientation;
    private boolean isTablet;

    @BindView(R.id.notes_recycler_view)
    RecyclerView notesRecyclerView;

    @BindView(R.id.category_spinner)
    Spinner categorySpinner;

    @BindView(R.id.remove_category)
    MaterialTextView removeCategory;

    public static NotesListFragment newInstance() {
        return new NotesListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_list_fragment, container, false);
        ButterKnife.bind(this, view);

        fragmentManager = getActivity().getSupportFragmentManager();

        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);

        adapter = new NotesListAdapter(new NotesListAdapter.NoteDiff(), this);
        notesRecyclerView.setAdapter(adapter);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryAdapter = new CategoryAdapter(getContext());
        categorySpinner.setAdapter(categoryAdapter);

        return view;
    }

    @OnItemSelected(R.id.category_spinner)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        removeCategory.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);

        Category category = (Category) parent.getItemAtPosition(position);
        mViewModel.setCategory(category);
    }

    @OnClick(R.id.add_category)
    public void onClickAddCategory() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        TextInputEditText textInput = new TextInputEditText(getContext());
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        textInput.setHint(R.string.add_category_hint);

        builder.setTitle(getString(R.string.add_category))
                .setView(textInput)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = textInput.getEditableText().toString().toUpperCase();
                        mViewModel.addNewCategory(input);
                    }
                });

        builder.create();
        builder.show();
    }

    @OnClick(R.id.remove_category)
    public void onClickRemoveCategory() {
        Category category = (Category) categorySpinner.getSelectedItem();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        builder.setTitle(getString(R.string.remove_category) + " " + category.name + "?")
                .setMessage(R.string.remove_category_message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mViewModel.removeCategory(category);
                    }
                });

        builder.create();
        builder.show();
    }

    @Override
    public void onNotesListItemClick(int position) {
        Bundle bundle = new Bundle();
        String noteId = adapter.getCurrentList().get(position).noteId;
        bundle.putString("noteId", noteId);

        DisplayNoteFragment fragment = new DisplayNoteFragment();
        fragment.setArguments(bundle);

        if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_right_container, fragment, RIGHT_CONTAINER_TAG)
                    .commit();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_left_container, fragment, LEFT_CONTAINER_TAG)
                    .addToBackStack(this.getClass().getName())
                    .commit();
        }
    }

    @OnClick(R.id.add_note_fab)
    public void onClickAddNote() {
        if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_right_container, new EditNotesFragment(), RIGHT_CONTAINER_TAG)
                    .commit();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_left_container, new EditNotesFragment(), LEFT_CONTAINER_TAG)
                    .addToBackStack(this.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(NotesListViewModel.class);

        mViewModel.getAllNotesFilteredByCategory().observe(this, notes -> {
            adapter.submitList(notes);
        });

        mViewModel.getAllCategories().observe(this, categories -> {
            categoryAdapter.clear();
            categoryAdapter.addAll(categories);
            Category allCategory = new Category();
            allCategory.name = getString(R.string.all_categories).toUpperCase();
            categoryAdapter.insert(allCategory, 0);
            categorySpinner.setSelection(0);
        });
    }

}