package id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Reminder;

public class ReminderListAdapter extends ListAdapter<Reminder, ReminderListAdapter.ReminderViewHolder> {
    public interface ListItemOnClickListener {
        void onListItemClick(int position);
    }
    final private ReminderListAdapter.ListItemOnClickListener listItemOnClickListener;

    public ReminderListAdapter(@NonNull DiffUtil.ItemCallback<Reminder> diffCallback, ListItemOnClickListener listItemOnClickListener) {
        super(diffCallback);
        this.listItemOnClickListener = listItemOnClickListener;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        Reminder current = getItem(position);
        holder.bind(current);
    }

    public static class ReminderDiff extends DiffUtil.ItemCallback<Reminder> {

        @Override
        public boolean areItemsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Reminder oldItem, @NonNull Reminder newItem) {
            return (oldItem.title.equals(newItem.title)) &&
                    (oldItem.time.equals(newItem.time)) &&
                    (oldItem.date.equals(newItem.date));
        }
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.reminders_item_title)
        MaterialTextView titleTextView;

        @BindView(R.id.reminders_item_date_time)
        MaterialTextView dateTimeTextView;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Reminder reminder) {
            titleTextView.setText(reminder.title);
            dateTimeTextView.setText(reminder.date + " " + reminder.time);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemOnClickListener.onListItemClick(position);
        }
    }
}
