package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;

public class EditNotesFragment extends Fragment {
    @BindView(R.id.edit_note_title)
    TextInputEditText editNoteTitle;

    @BindView(R.id.edit_note_description)
    TextInputEditText editNoteDescription;

    private FragmentManager fragmentManager;
    private EditNotesViewModel mViewModel;
    private View view;
    private Note editableNote;

    public static EditNotesFragment newInstance() {
        return new EditNotesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.edit_notes_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void setupToolbar() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_note_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                fragmentManager.popBackStack();
                return true;
            case R.id.edit_note_done:
                editableNote.title = editNoteTitle.getText().toString();
                editableNote.description = editNoteDescription.getText().toString();
                mViewModel.saveNote(editableNote);
                Log.d("ir", "save_note");
                return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar();
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(EditNotesViewModel.class);
        editableNote = mViewModel.initNote();
    }

}