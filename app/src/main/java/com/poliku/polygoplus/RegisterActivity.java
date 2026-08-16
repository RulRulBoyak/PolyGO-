package com.poliku.polygoplus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.text.TextUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.poliku.polygoplus.data.AppDataStore;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppDataStore.initialize(this);

        findViewById(R.id.btnRegisterAction).setOnClickListener(v -> {
            String name = text(R.id.etFullName); String studentId = text(R.id.etMatrixNo); String email = text(R.id.etEmail); String password = text(R.id.etPassword);
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(studentId) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) { Toast.makeText(this, "Complete all fields", Toast.LENGTH_SHORT).show(); return; }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) { ((TextInputEditText)findViewById(R.id.etEmail)).setError("Enter a valid email"); return; }
            if (password.length() < 6) { ((TextInputEditText)findViewById(R.id.etPassword)).setError("Use at least 6 characters"); return; }
            if (!AppDataStore.register(this, name, studentId, email, password)) { Toast.makeText(this, "An account already exists on this device", Toast.LENGTH_SHORT).show(); return; }
            Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        findViewById(R.id.tvLoginLink).setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private String text(int id) { TextInputEditText input = findViewById(id); return input.getText() == null ? "" : input.getText().toString().trim(); }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
