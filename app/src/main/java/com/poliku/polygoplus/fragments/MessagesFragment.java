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

        seedMessages();
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.buttonCompose).setOnClickListener(v ->
                Toast.makeText(requireContext(), "New message coming soon", Toast.LENGTH_SHORT).show());

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
        messages.add(new Message("AM", "Aina Mahmud", "Is the study table still available?", "Now", true));
        messages.add(new Message("RK", "Ryan Koh", "Thanks, I can meet you near campus.", "12m", false));
        messages.add(new Message("NS", "Nur Syafiqah", "I have sent the details for the service.", "1h", true));
        messages.add(new Message("JT", "Jason Tan", "Can you share a few more photos?", "Yesterday", false));
        messages.add(new Message("LH", "Liam Harrison", "Your offer was accepted. Great deal!", "Mon", false));
    }

    private static class Message {
        final String initials, sender, preview, time;
        final boolean unread;
        Message(String initials, String sender, String preview, String time, boolean unread) {
            this.initials = initials; this.sender = sender; this.preview = preview; this.time = time; this.unread = unread;
        }
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
            h.itemView.setOnClickListener(v -> Toast.makeText(v.getContext(), "Opening chat with " + m.sender, Toast.LENGTH_SHORT).show());
        }
        @Override public int getItemCount() { return visibleMessages.size(); }
        static class Holder extends RecyclerView.ViewHolder {
            final TextView avatar, sender, preview, time, unread;
            Holder(View view) { super(view); avatar = view.findViewById(R.id.textViewAvatar); sender = view.findViewById(R.id.textViewSender); preview = view.findViewById(R.id.textViewPreview); time = view.findViewById(R.id.textViewTime); unread = view.findViewById(R.id.textViewUnread); }
        }
    }
}
