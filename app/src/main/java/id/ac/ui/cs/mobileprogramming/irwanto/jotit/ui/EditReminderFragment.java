package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
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

import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.databinding.EditReminderFragmentBinding;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.service.RemainingTimeService;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.components.EditReminderGLViewRenderer;

public class EditReminderFragment extends Fragment {

    private EditReminderViewModel mViewModel;
    private FragmentManager fragmentManager;
    private EditReminderFragmentBinding binding;
    private String reminderId;
    private boolean isEdit = false;
    private int orientation;
    private boolean isTablet;
    private Intent remainingTimeIntent;

    @BindView(R.id.edit_reminder_gl_view)
    GLSurfaceView glView;

    @BindView(R.id.edit_reminder_remaining_time)
    MaterialTextView remainingTimeView;

    @BindView(R.id.edit_reminder_notify_time_message)
    MaterialTextView notifyTimeMessageView;

    static {
        System.loadLibrary("formatter");
    }

    private BroadcastReceiver remainingTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                updateRemainingTimeUI(intent);
            }
        }
    };

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

        if (isEdit) {
            glView.setEGLContextClientVersion(2);
            glView.setRenderer(new EditReminderGLViewRenderer(getContext()));
        }

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

                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd/MM/yyyy HH:mm");
                try {
                    Date datetime = simpleDateFormat.parse(reminder.date + " " + reminder.time);
                    remainingTimeIntent = new Intent(getActivity(), RemainingTimeService.class);
                    remainingTimeIntent.putExtra("target_time", datetime.getTime());
                    getActivity().startService(remainingTimeIntent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            getActivity().registerReceiver(remainingTimeReceiver, new IntentFilter(RemainingTimeService.REMAINING_TIME_BR));
        }
    }

    private void updateRemainingTimeUI(Intent intent) {
        if (intent.getExtras() != null) {
            long remainingTime = intent.getLongExtra("remaining_time", System.currentTimeMillis());
            if (remainingTime >= 0) {
                glView.setVisibility(View.VISIBLE);
                remainingTimeView.setVisibility(View.VISIBLE);
                String formattedTime = getFormattedRemainingTime(remainingTime, getContext().getString(R.string.hours), getContext().getString(R.string.minutes), getContext().getString(R.string.seconds));
                remainingTimeView.setText(formattedTime);
                notifyTimeMessageView.setText(R.string.reminder_remaining_time_message);
            } else {
                glView.setVisibility(View.GONE);
                remainingTimeView.setVisibility(View.GONE);
                notifyTimeMessageView.setText(R.string.reminder_done_message);
            }
        }
    }

    public native String getFormattedRemainingTime(long remainingTime, String hours, String minutes, String seconds);

    @Override
    public void onDetach() {
        super.onDetach();
        if (isEdit) {
            getActivity().unregisterReceiver(remainingTimeReceiver);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
        setupToolbar(false);
        if (isEdit && remainingTimeIntent != null) {
            getActivity().stopService(remainingTimeIntent);
        }
    }
}