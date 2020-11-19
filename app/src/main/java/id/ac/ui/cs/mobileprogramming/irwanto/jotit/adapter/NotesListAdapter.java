package id.ac.ui.cs.mobileprogramming.irwanto.jotit.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.R;
import id.ac.ui.cs.mobileprogramming.irwanto.jotit.model.Note;

public class NotesListAdapter extends ListAdapter<Note, NotesListAdapter.NoteViewHolder> {
    public interface ListItemOnClickListener {
        void onNotesListItemClick(int position);
    }
    final private ListItemOnClickListener listItemOnClickListener;

    public NotesListAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback, ListItemOnClickListener listItemOnClickListener) {
        super(diffCallback);
        this.listItemOnClickListener = listItemOnClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note current = getItem(position);
        holder.bind(current.title);
    }

    public static class NoteDiff extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.title.equals(newItem.title);
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.notes_item_title)
        TextView noteItemTitleView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String text) {
            noteItemTitleView.setText(text);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemOnClickListener.onNotesListItemClick(position);
        }
    }
}