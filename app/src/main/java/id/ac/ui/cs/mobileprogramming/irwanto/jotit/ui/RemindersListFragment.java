package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter.ReminderListAdapter;

import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.LEFT_CONTAINER_TAG;
import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.RIGHT_CONTAINER_TAG;

public class RemindersListFragment extends Fragment implements ReminderListAdapter.ListItemOnClickListener {

    private RemindersListViewModel mViewModel;
    private FragmentManager fragmentManager;
    private ReminderListAdapter adapter;
    private int orientation;
    private boolean isTablet;

    @BindView(R.id.reminders_list_recycler_view)
    RecyclerView recyclerView;

    public static RemindersListFragment newInstance() {
        return new RemindersListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reminders_list_fragment, container, false);
        ButterKnife.bind(this, view);

        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);

        fragmentManager = getActivity().getSupportFragmentManager();

        adapter = new ReminderListAdapter(new ReminderListAdapter.ReminderDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @OnClick(R.id.add_reminder_fab)
    public void addReminder() {
        if (isTablet || orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_right_container, new EditReminderFragment(), RIGHT_CONTAINER_TAG)
                    .addToBackStack(this.getClass().getName())
                    .commit();
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_left_container, new EditReminderFragment(), LEFT_CONTAINER_TAG)
                    .addToBackStack(this.getClass().getName())
                    .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Bundle bundle = new Bundle();
        String reminderId = adapter.getCurrentList().get(position).reminderId;
        bundle.putString("reminderId", reminderId);

        EditReminderFragment fragment = new EditReminderFragment();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication())).get(RemindersListViewModel.class);
        mViewModel.getAllReminder().observe(this, reminders -> {
            adapter.submitList(reminders);
        });
    }
}