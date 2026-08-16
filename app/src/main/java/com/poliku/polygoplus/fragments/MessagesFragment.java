package com.poliku.polygoplus.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.poliku.polygoplus.R;
import com.poliku.polygoplus.ChatActivity;
import com.poliku.polygoplus.SearchActivity;
import com.poliku.polygoplus.data.AppDataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessagesFragment extends androidx.fragment.app.Fragment {

    private final List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvMessages);

        AppDataStore.initialize(requireContext());
        seedMessages();
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.buttonCompose).setOnClickListener(v -> startActivity(new android.content.Intent(requireContext(), SearchActivity.class)));

        TextView search = view.findViewById(R.id.editTextMessageSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adapter.filter(s.toString()); }
            @Override public void afterTextChanged(Editable s) { }
        });

        Chip unread = view.findViewById(R.id.chipUnread);
        unread.setOnCheckedChangeListener((button, checked) -> { if (checked) adapter.showUnreadOnly(); });
        view.findViewById(R.id.chipAll).setOnClickListener(v -> adapter.resetFilter());
        view.findViewById(R.id.chipBuying).setOnClickListener(v -> adapter.filter(""));
        return view;
    }

    private void seedMessages() {
        messages.clear();
        for (AppDataStore.ThreadRecord thread : AppDataStore.getThreads(requireContext())) {
            messages.add(new Message(thread.id, thread.name, thread.preview, "", false));
        }
    }

    private static class Message {
        final String id, initials, sender, preview, time;
        final boolean unread;
        Message(String id, String sender, String preview, String time, boolean unread) {
            this.id = id; this.initials = initialsFor(sender); this.sender = sender; this.preview = preview; this.time = time; this.unread = unread;
        }
        private static String initialsFor(String name) { String[] parts=name.trim().split("\\s+"); if(parts.length==1)return parts[0].substring(0,Math.min(2,parts[0].length())).toUpperCase(Locale.ROOT); return (parts[0].substring(0,1)+parts[parts.length-1].substring(0,1)).toUpperCase(Locale.ROOT); }
    }

    private static class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {
        private final List<Message> allMessages;
        private final List<Message> visibleMessages = new ArrayList<>();

        MessageAdapter(List<Message> messages) { allMessages = messages; visibleMessages.addAll(messages); }
        void resetFilter() { visibleMessages.clear(); visibleMessages.addAll(allMessages); notifyDataSetChanged(); }
        void showUnreadOnly() { visibleMessages.clear(); for (Message m : allMessages) if (m.unread) visibleMessages.add(m); notifyDataSetChanged(); }
        void filter(String query) {
            String value = query.toLowerCase(Locale.ROOT).trim();
            visibleMessages.clear();
            for (Message m : allMessages) if (m.sender.toLowerCase(Locale.ROOT).contains(value) || m.preview.toLowerCase(Locale.ROOT).contains(value)) visibleMessages.add(m);
            notifyDataSetChanged();
        }
        @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
        }
        @Override public void onBindViewHolder(@NonNull Holder h, int position) {
            Message m = visibleMessages.get(position);
            h.avatar.setText(m.initials); h.sender.setText(m.sender); h.preview.setText(m.preview); h.time.setText(m.time);
            h.sender.setTextColor(Color.parseColor(m.unread ? "#222222" : "#717171"));
            h.unread.setVisibility(m.unread ? View.VISIBLE : View.GONE);
            h.itemView.setOnClickListener(v -> { android.content.Intent intent = new android.content.Intent(v.getContext(), ChatActivity.class); intent.putExtra(ChatActivity.EXTRA_THREAD_ID, m.id); v.getContext().startActivity(intent); });
        }
        @Override public int getItemCount() { return visibleMessages.size(); }
        static class Holder extends RecyclerView.ViewHolder {
            final TextView avatar, sender, preview, time, unread;
            Holder(View view) { super(view); avatar = view.findViewById(R.id.textViewAvatar); sender = view.findViewById(R.id.textViewSender); preview = view.findViewById(R.id.textViewPreview); time = view.findViewById(R.id.textViewTime); unread = view.findViewById(R.id.textViewUnread); }
        }
    }
}
