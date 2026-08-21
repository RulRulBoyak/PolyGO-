package com.poliku.polygoplus.data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poliku.polygoplus.ChatActivity;
import com.poliku.polygoplus.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.Holder> {
    public enum Filter { ALL, UNREAD, BUYING }
    private final List<AppDataStore.ThreadRecord> all = new ArrayList<>();
    private final List<AppDataStore.ThreadRecord> visible = new ArrayList<>();
    private String query = "";
    private Filter filter = Filter.ALL;

    public void submit(List<AppDataStore.ThreadRecord> threads) {
        all.clear(); all.addAll(threads);
        all.sort(Comparator.comparingLong((AppDataStore.ThreadRecord t) -> t.lastMessageTime).reversed());
        applyFilter();
    }

    public void setQuery(String value) { query = value == null ? "" : value.trim().toLowerCase(Locale.ROOT); applyFilter(); }
    public void setFilter(Filter value) { filter = value == null ? Filter.ALL : value; applyFilter(); }

    private void applyFilter() {
        visible.clear();
        for (AppDataStore.ThreadRecord thread : all) {
            boolean textMatch = query.isEmpty()
                    || thread.name.toLowerCase(Locale.ROOT).contains(query)
                    || thread.preview.toLowerCase(Locale.ROOT).contains(query);
            boolean filterMatch = filter == Filter.ALL
                    || (filter == Filter.UNREAD && thread.unread)
                    || (filter == Filter.BUYING && !thread.listingId.isEmpty());
            if (textMatch && filterMatch) visible.add(thread);
        }
        notifyDataSetChanged();
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        AppDataStore.ThreadRecord thread = visible.get(position);
        holder.avatar.setText(initials(thread.name));
        holder.sender.setText(thread.name);
        holder.preview.setText(thread.preview.isEmpty() ? "No messages yet" : thread.preview);
        holder.time.setText(formatTime(thread.lastMessageTime));
        holder.sender.setTextColor(thread.unread ? 0xFF222222 : 0xFF717171);
        holder.preview.setTextColor(thread.unread ? 0xFF222222 : 0xFF717171);
        holder.unread.setVisibility(thread.unread ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(v -> {
            AppDataStore.markThreadRead(v.getContext(), thread.id);
            android.content.Intent intent = new android.content.Intent(v.getContext(), ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_THREAD_ID, thread.id);
            v.getContext().startActivity(intent);
        });
    }

    private String formatTime(long timestamp) {
        if (timestamp <= 0) return "";
        return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    private String initials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) return "?";
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase(Locale.ROOT);
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase(Locale.ROOT);
    }

    @Override public int getItemCount() { return visible.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView avatar, sender, preview, time, unread;
        Holder(View view) { super(view); avatar=view.findViewById(R.id.textViewAvatar); sender=view.findViewById(R.id.textViewSender); preview=view.findViewById(R.id.textViewPreview); time=view.findViewById(R.id.textViewTime); unread=view.findViewById(R.id.textViewUnread); }
    }
}
