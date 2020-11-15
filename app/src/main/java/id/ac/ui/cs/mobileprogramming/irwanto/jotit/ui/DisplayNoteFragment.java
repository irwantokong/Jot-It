package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
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

import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.DisplayNoteFragmentBinding;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;

public class DisplayNoteFragment extends Fragment {

    private DisplayNoteViewModel mViewModel;
    private DisplayNoteFragmentBinding binding;
    private FragmentManager fragmentManager;
    private String noteId;

    public static DisplayNoteFragment newInstance() {
        return new DisplayNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        noteId = this.getArguments().getString("noteId");
        Log.d("noteid", noteId);
        fragmentManager = getActivity().getSupportFragmentManager();
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.display_note_fragment, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.display_note_options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fragmentManager.popBackStack();
                return true;
            case R.id.display_note_delete:
                Log.d("d", "delete");
                return true;
            case R.id.display_note_edit:
                Log.d("d", "edit");
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar(true);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(DisplayNoteViewModel.class);
        mViewModel.setNote(noteId);
        binding.setViewModel(mViewModel);
    }

    public void setupToolbar(boolean home) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(home);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupToolbar(false);
    }
}