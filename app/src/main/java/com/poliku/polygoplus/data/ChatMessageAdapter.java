package com.poliku.polygoplus.data;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poliku.polygoplus.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.Holder> {
    private final List<JSONObject> messages = new ArrayList<>();
    private final String currentUser;

    public ChatMessageAdapter(String currentUser) { this.currentUser = currentUser == null ? "" : currentUser; }

    public void submit(JSONArray source) {
        messages.clear();
        if (source != null) for (int i = 0; i < source.length(); i++) {
            JSONObject message = source.optJSONObject(i);
            if (message != null) messages.add(message);
        }
        notifyDataSetChanged();
    }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        JSONObject message = messages.get(position);
        boolean mine = message.optBoolean("mine", currentUser.equals(message.optString("sender")));
        holder.body.setText(message.optString("text"));
        holder.time.setText(formatTime(message.optLong("time", 0)));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.bubble.getLayoutParams();
        params.gravity = mine ? Gravity.END : Gravity.START;
        holder.bubble.setLayoutParams(params);
        holder.bubble.setBackgroundResource(mine ? R.drawable.bg_chat_bubble_mine : R.drawable.bg_chat_bubble_other);
        holder.body.setTextColor(mine ? Color.WHITE : Color.parseColor("#222222"));
        holder.time.setTextColor(mine ? 0xCCFFFFFF : Color.parseColor("#717171"));
        holder.bubble.setGravity(mine ? Gravity.END : Gravity.START);
    }

    private String formatTime(long timestamp) {
        if (timestamp <= 0) return "";
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(timestamp));
    }

    @Override public int getItemCount() { return messages.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final LinearLayout bubble;
        final TextView body, time;
        Holder(View view) { super(view); bubble=view.findViewById(R.id.messageBubble); body=view.findViewById(R.id.messageBody); time=view.findViewById(R.id.messageTime); }
    }
}
