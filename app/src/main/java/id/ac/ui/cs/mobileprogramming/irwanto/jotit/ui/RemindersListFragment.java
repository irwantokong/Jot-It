package id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter.ReminderListAdapter;

public class RemindersListFragment extends Fragment implements ReminderListAdapter.ListItemOnClickListener {

    private RemindersListViewModel mViewModel;
    private FragmentManager fragmentManager;
    private ReminderListAdapter adapter;
    private int orientation;

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

        fragmentManager = getActivity().getSupportFragmentManager();

        adapter = new ReminderListAdapter(new ReminderListAdapter.ReminderDiff(), this::onListItemClick);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @OnClick(R.id.add_reminder_fab)
    public void addReminder() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.replace(R.id.activity_container, new EditReminderFragment());
            fragmentTransaction.addToBackStack(this.getClass().getName());
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.replace(R.id.activity_right_container, new EditReminderFragment(), "right_container");
            fragmentTransaction.addToBackStack(this.getClass().getName());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onListItemClick(int position) {
        Bundle bundle = new Bundle();
        String reminderId = adapter.getCurrentList().get(position).reminderId;
        bundle.putString("reminderId", reminderId);

        EditReminderFragment fragment = new EditReminderFragment();
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragmentTransaction.replace(R.id.activity_container, fragment);
            fragmentTransaction.addToBackStack(this.getClass().getName());
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.replace(R.id.activity_right_container, fragment, "right_container");
        }
        fragmentTransaction.commit();
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