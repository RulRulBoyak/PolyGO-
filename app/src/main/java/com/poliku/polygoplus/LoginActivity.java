package com.poliku.polygoplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.poliku.polygoplus.data.AppDataStore;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppDataStore.initialize(this);

        findViewById(R.id.btnLoginNormal).setOnClickListener(v -> {
            String studentId = ((com.google.android.material.textfield.TextInputEditText)findViewById(R.id.etMatrixNo)).getText().toString().trim();
            String password = ((com.google.android.material.textfield.TextInputEditText)findViewById(R.id.etPassword)).getText().toString();
            if (studentId.isEmpty() || password.isEmpty()) { Toast.makeText(this, "Enter your ID and password", Toast.LENGTH_SHORT).show(); return; }
            if (!AppDataStore.login(this, studentId, password)) { Toast.makeText(this, "Incorrect ID or password", Toast.LENGTH_SHORT).show(); return; }
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
        findViewById(R.id.btnLoginGoogle).setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
