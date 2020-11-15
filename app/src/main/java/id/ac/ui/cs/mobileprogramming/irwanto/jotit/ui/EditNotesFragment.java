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
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.EditNotesFragmentBinding;

public class EditNotesFragment extends Fragment {
    private FragmentManager fragmentManager;
    private EditNotesViewModel mViewModel;
    private EditNotesFragmentBinding binding;

    public static EditNotesFragment newInstance() {
        return new EditNotesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentManager = getActivity().getSupportFragmentManager();
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_notes_fragment, container, false);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    public void setupToolbar(boolean home) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(home);
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
                mViewModel.saveNote();
                Log.d("ir", "save_note");
                return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar(true);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(EditNotesViewModel.class);
        binding.setViewModel(mViewModel);
        mViewModel.initNote();
    }

}