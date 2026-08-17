package com.poliku.polygoplus;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.poliku.polygoplus.data.AppDataStore;
import org.json.JSONException;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_THREAD_ID = "thread_id";
    private String threadId;
    private TextView messages;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        threadId = getIntent().getStringExtra(EXTRA_THREAD_ID);
        messages = findViewById(R.id.chatMessages);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        render();

        MaterialButton send = findViewById(R.id.btnSend);
        EditText input = findViewById(R.id.etMessage);

        send.setOnClickListener(v -> {
            String text = input.getText().toString().trim();
            if (text.isEmpty()) {
                input.setError("Write a message");
                return;
            }
            AppDataStore.sendMessage(this, threadId, text);
            input.setText("");
            render();
        });
    }

    private void render() {
        AppDataStore.ThreadRecord t = AppDataStore.getThread(this, threadId);
        if (t == null) {
            Toast.makeText(this, "Conversation not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ((TextView) findViewById(R.id.chatTitle)).setText(t.name);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < t.messages.length(); i++)
            try {
                org.json.JSONObject m = t.messages.getJSONObject(i);
                output.append(m.optString("sender")).append(": ").append(m.optString("text")).append("\n\n");
            } catch (JSONException ignored) {
            }
        messages.setText(output.toString().trim());
    }
}
