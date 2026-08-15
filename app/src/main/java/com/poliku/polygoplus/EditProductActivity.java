package com.poliku.polygoplus;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class EditProductActivity extends AppCompatActivity {

    private TextInputEditText etPrice;
    private double currentPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        setupToolbar();
        setupPriceAdjuster();
        setupCategoryDropdown();
        setupPublishAction();
    }

    private void setupToolbar() {
        findViewById(R.id.toolbar).setOnClickListener(v -> finish());
    }

    private void setupPriceAdjuster() {
        etPrice = findViewById(R.id.etPrice);
        
        findViewById(R.id.btnPricePlus).setOnClickListener(v -> {
            currentPrice += 1.0;
            updatePriceDisplay();
        });

        findViewById(R.id.btnPriceMinus).setOnClickListener(v -> {
            if (currentPrice >= 1.0) {
                currentPrice -= 1.0;
                updatePriceDisplay();
            }
        });
    }

    private void updatePriceDisplay() {
        etPrice.setText(String.format("%.2f", currentPrice));
    }

    private void setupCategoryDropdown() {
        String[] categories = {"Electronics", "Fashion", "Home", "Books", "Services", "Others"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        AutoCompleteTextView autoComplete = findViewById(R.id.autoCompleteCategory);
        autoComplete.setAdapter(adapter);
    }

    private void setupPublishAction() {
        findViewById(R.id.btnSaveProduct).setOnClickListener(v -> {
            Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}