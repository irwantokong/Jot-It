package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.EditReminderFragmentBinding;

public class EditReminderFragment extends Fragment {

    private EditReminderViewModel mViewModel;
    private FragmentManager fragmentManager;
    private EditReminderFragmentBinding binding;
    private String reminderId;
    private boolean isEdit = false;
    private int orientation;
    private boolean isTablet;

    public static EditReminderFragment newInstance() {
        return new EditReminderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (this.getArguments() != null) {
            reminderId = this.getArguments().getString("reminderId", null);
            isEdit = true;
        }
        setHasOptionsMenu(true);
        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);

        fragmentManager = getActivity().getSupportFragmentManager();

        binding = DataBindingUtil.inflate(inflater, R.layout.edit_reminder_fragment, container, false);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_reminder_options_menu, menu);
        MenuItem deleteOption = menu.findItem(R.id.edit_reminder_delete);
        deleteOption.setVisible(isEdit);
    }

    @OnClick(R.id.edit_reminder_date_wrapper)
    public void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mViewModel.setDateField(year, month + 1, day);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @OnClick(R.id.edit_reminder_time_wrapper)
    public void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mViewModel.setTimeField(hour, minute);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fragmentManager.popBackStack();
                return true;
            case R.id.edit_reminder_done:
                mViewModel.saveReminder(isEdit);
                if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fragmentManager.beginTransaction().remove(this).commit();
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fragmentManager.popBackStack();
                }
                return true;
            case R.id.edit_reminder_delete:
                mViewModel.deleteReminder();
                if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fragmentManager.beginTransaction().remove(this).commit();
                } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    fragmentManager.popBackStack();
                }
                return true;
        }
        return false;
    }

    public void setupToolbar(boolean showHome) {
        showHome = (!isTablet && orientation == Configuration.ORIENTATION_PORTRAIT && showHome);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(showHome);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupToolbar(true);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(EditReminderViewModel.class);
        binding.setViewModel(mViewModel);

        mViewModel.initReminder(reminderId, isEdit);
        if (isEdit) {
            mViewModel.getLoadedReminder().observe(this, reminder -> {
                mViewModel.setEditableReminder(reminder);
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        setupToolbar(false);
    }
}