package id.ac.ui.cs.mobileprogramming.irwanto.jotit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.DisplayNoteFragment;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.EditNotesFragment;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.EditReminderFragment;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.NotesListFragment;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.ui.RemindersListFragment;

import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.LEFT_CONTAINER_TAG;
import static id.ac.ui.cs.mobileprogramming.irwanto.jotit.util.Constants.RIGHT_CONTAINER_TAG;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private int orientation;
    private boolean isTablet;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        orientation = getResources().getConfiguration().orientation;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        Log.d("isTablet", "" + isTablet);

        if (savedInstanceState == null) {
            loadFragment(new NotesListFragment());
        } else {
            if (!isTablet) {
                if (fragmentManager.findFragmentByTag(LEFT_CONTAINER_TAG) != null) {
                    Fragment f = fragmentManager.findFragmentByTag(LEFT_CONTAINER_TAG);
                    if (f instanceof EditReminderFragment) {
                        fragmentManager.beginTransaction().remove(f).commit();
                        loadFragment(new RemindersListFragment());
                    } else if ((f instanceof EditNotesFragment) || (f instanceof DisplayNoteFragment)) {
                        fragmentManager.beginTransaction().remove(f).commit();
                        loadFragment(new NotesListFragment());
                    }
                }

                if (fragmentManager.findFragmentByTag(RIGHT_CONTAINER_TAG) != null) {
                    Fragment f = fragmentManager.findFragmentByTag(RIGHT_CONTAINER_TAG);
                    fragmentManager.beginTransaction().remove(f).commit();
                }
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        createNotificationChannel();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        if (isTablet || (orientation == Configuration.ORIENTATION_LANDSCAPE)) {
            if (fragmentManager.findFragmentByTag(RIGHT_CONTAINER_TAG) != null) {
                Fragment f = fragmentManager.findFragmentByTag(RIGHT_CONTAINER_TAG);
                fragmentManager.beginTransaction().remove(f).commit();
            }
        }

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.notes_page:
                fragment = new NotesListFragment();
                break;
            case R.id.reminders_page:
                fragment = new RemindersListFragment();
                break;
        }

        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_left_container, fragment, LEFT_CONTAINER_TAG)
                    .commit();
            return true;
        }
        return false;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = getString(R.string.notification_channel_name);
            String channelId = getString(R.string.notification_channel_id);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}