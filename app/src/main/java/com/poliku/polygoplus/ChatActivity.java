package com.poliku.polygoplus;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.ChatMessageAdapter;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_THREAD_ID = "thread_id";
    private String threadId;
    private RecyclerView messageList;
    private ChatMessageAdapter adapter;
    private EditText input;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        threadId = getIntent().getStringExtra(EXTRA_THREAD_ID);
        if (threadId == null || threadId.trim().isEmpty()) { finish(); return; }
        messageList = findViewById(R.id.chatMessages);
        messageList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatMessageAdapter(AppDataStore.userName(this));
        messageList.setAdapter(adapter);
        input = findViewById(R.id.etMessage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnSend).setOnClickListener(v -> sendMessage());
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) { sendMessage(); return true; }
            return false;
        });
        AppDataStore.markThreadRead(this, threadId);
        render();
    }

    @Override protected void onResume() {
        super.onResume();
        if (adapter != null) { AppDataStore.markThreadRead(this, threadId); render(); }
    }

    private void sendMessage() {
        String text = input.getText().toString().trim();
        if (text.isEmpty()) { input.setError("Write a message"); return; }
        AppDataStore.sendMessage(this, threadId, text);
        input.setText("");
        render();
    }

    private void render() {
        AppDataStore.ThreadRecord thread = AppDataStore.getThread(this, threadId);
        if (thread == null) {
            Toast.makeText(this, "Conversation not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.chatTitle)).setText(thread.name);
        adapter.submit(thread.messages);
        if (adapter.getItemCount() > 0) messageList.scrollToPosition(adapter.getItemCount() - 1);
    }
}
