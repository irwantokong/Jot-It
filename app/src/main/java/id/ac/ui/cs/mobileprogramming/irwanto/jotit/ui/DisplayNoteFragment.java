package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.DisplayNoteFragmentBinding;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.ConnectivityUtil;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.LEFT_CONTAINER_TAG;
import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.RIGHT_CONTAINER_TAG;

public class DisplayNoteFragment extends Fragment {

    private DisplayNoteViewModel mViewModel;
    private DisplayNoteFragmentBinding binding;
    private FragmentManager fragmentManager;
    private String noteId;
    private int orientation;
    private boolean isTablet;

    @BindView(R.id.display_note_image)
    ImageView imageView;

    @BindView(R.id.display_note_description)
    TextView descriptionView;

    public static DisplayNoteFragment newInstance() {
        return new DisplayNoteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        noteId = this.getArguments().getString("noteId");

        fragmentManager = getActivity().getSupportFragmentManager();

        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        setHasOptionsMenu(true);

        binding = DataBindingUtil.inflate(inflater, R.layout.display_note_fragment, container, false);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
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
                mViewModel.deleteNote();
                if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fragmentManager.beginTransaction().remove(this).commit();
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fragmentManager.popBackStack();
                }
                return true;
            case R.id.display_note_edit:
                Bundle bundle = new Bundle();
                bundle.putString("noteId", noteId);

                EditNotesFragment fragment = new EditNotesFragment();
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
                return true;
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

        mViewModel.viewedNote.observe(this, note -> {
            loadImage(note.imagePath);
            binding.setNote(note);
            binding.executePendingBindings();
            setDescriptionViewLinkMovement();
        });
    }

    public void setDescriptionViewLinkMovement() {
        BetterLinkMovementMethod method = BetterLinkMovementMethod.linkify(Linkify.WEB_URLS, descriptionView);
        method.setOnLinkClickListener((textView, url) -> {
            ConnectivityUtil connectivityUtil = new ConnectivityUtil(getContext());

            if (connectivityUtil.isNetworkConnected()) {
                return false;
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
                builder.setTitle(R.string.not_connected_dialog_title)
                        .setMessage(R.string.not_connected_dialog_message)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                           dialogInterface.cancel();
                        });
                builder.show();
            }

            return true;
        });
    }

    public void loadImage(String imagePath) {
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()){
                Glide.with(this).load(imageFile).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
            }
        }
    }

    public void setupToolbar(boolean showHome) {
        showHome = (!isTablet && orientation == Configuration.ORIENTATION_PORTRAIT && showHome);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setupToolbar(false);
        binding = null;
    }
}