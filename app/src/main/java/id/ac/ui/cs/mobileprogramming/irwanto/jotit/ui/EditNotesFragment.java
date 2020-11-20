package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.MainActivity;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter.CategoryAdapter;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.EditNotesFragmentBinding;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Category;

import static android.app.Activity.RESULT_OK;

public class EditNotesFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 21;

    private FragmentManager fragmentManager;
    private EditNotesViewModel mViewModel;
    private EditNotesFragmentBinding binding;
    private CategoryAdapter categoryAdapter;
    private String noteId;
    private boolean isEdit;
    private String currentPhotoPath;
    private List<Category> categoryList;

    @BindView(R.id.edit_note_image)
    ImageView imageView;

    @BindView(R.id.edit_note_category_spinner)
    Spinner categorySpinner;

    public static EditNotesFragment newInstance() {
        return new EditNotesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            noteId = this.getArguments().getString("noteId", null);
            isEdit = true;
        }

        fragmentManager = getActivity().getSupportFragmentManager();

        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_notes_fragment, container, false);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        categoryAdapter = new CategoryAdapter(getContext());
        categorySpinner.setAdapter(categoryAdapter);

        return view;
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
                mViewModel.saveNote(isEdit);
                fragmentManager.popBackStack();
                return true;
            case R.id.edit_note_take_picture:
                dispatchTakePictureIntent();
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

        mViewModel.getAllCategories().observe(this, categories -> {
            categoryList = categories;
            Category noCategory = new Category();
            noCategory.name = getString(R.string.no_category).toUpperCase();
            categoryList.add(0, noCategory);
            categoryAdapter.clear();
            categoryAdapter.addAll(categories);
        });

        mViewModel.initNote(noteId, isEdit);
        if (isEdit) {
            mViewModel.getLoadedNote().observe(this, note -> {
                if (note != null) {
                    mViewModel.setEditableNote(note);
                    loadImage();

                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).name.equals(note.category.name)) {
                            categorySpinner.setSelection(i);
                            break;
                        }
                    }
                }
            });
        }
    }

    @OnItemSelected(R.id.edit_note_category_spinner)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Category category = (Category) parent.getItemAtPosition(position);
        mViewModel.setCategory(category);
    }

    public void setupToolbar(boolean showHome) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
    }

    private void loadImage() {
        currentPhotoPath = mViewModel.getImageFilePath();
        if (currentPhotoPath != null) {
            File imageFile = new File(currentPhotoPath);
            if (imageFile.exists()) {
                Glide.with(this).load(imageFile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
            }
        }
    }

    public void dispatchTakePictureIntent() {
        MainActivity activity = (MainActivity) getActivity();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                        "id.ac.ui.cs.mobileprogramming.irwanto.jotit.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() {
        String imageFileName = mViewModel.getNoteImageFilename();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File imageFile = new File(currentPhotoPath);
            if (imageFile.exists()) {
                Glide.with(this).load(imageFile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
            }
            mViewModel.setImageFilePath(currentPhotoPath);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupToolbar(false);
        binding = null;
    }
}