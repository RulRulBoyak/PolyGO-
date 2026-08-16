package com.poliku.polygoplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.poliku.polygoplus.data.AppDataStore;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String EXTRA_LISTING_ID = "listing_id";
    private AppDataStore.ProductRecord product;
    private com.google.android.material.button.MaterialButton saveButton;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); setContentView(R.layout.activity_product_detail);
        product = AppDataStore.getListing(this, getIntent().getStringExtra(EXTRA_LISTING_ID));
        if (product == null) { finish(); return; }
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        ShapeableImageView image = findViewById(R.id.productImage); if (product.imageUri.isEmpty()) image.setImageResource(product.imageRes); else image.setImageURI(android.net.Uri.parse(product.imageUri));
        ((TextView)findViewById(R.id.productTitle)).setText(product.title);
        ((TextView)findViewById(R.id.productPrice)).setText("RM " + product.price);
        ((TextView)findViewById(R.id.productMeta)).setText("★ " + product.rating + "  •  " + product.distance + "  •  " + product.category);
        ((TextView)findViewById(R.id.productDescription)).setText(product.description);
        ((TextView)findViewById(R.id.sellerName)).setText(product.seller);
        saveButton = findViewById(R.id.btnSave);
        saveButton.setSelected(AppDataStore.isFavorite(this, product.id));
        saveButton.setOnClickListener(v -> { AppDataStore.toggleFavorite(this, product.id); v.setSelected(AppDataStore.isFavorite(this, product.id)); Toast.makeText(this, v.isSelected()?"Saved to your items":"Removed from saved items", Toast.LENGTH_SHORT).show(); });
        MaterialButton message = findViewById(R.id.btnMessageSeller);
        if (product.owner) { message.setText("This is your listing"); message.setEnabled(false); findViewById(R.id.btnMakeOffer).setEnabled(false); MaterialButton sold = findViewById(R.id.btnMarkSold); sold.setVisibility(android.view.View.VISIBLE); sold.setEnabled(product.available); sold.setOnClickListener(v -> { AppDataStore.markSold(this, product.id); Toast.makeText(this, "Listing marked as sold", Toast.LENGTH_SHORT).show(); finish(); }); }
        message.setOnClickListener(v -> { String id = AppDataStore.addThread(this, product.id, product.seller, "Hi, I am interested in your " + product.title + "."); Intent i = new Intent(this, ChatActivity.class); i.putExtra(ChatActivity.EXTRA_THREAD_ID, id); startActivity(i); });
        findViewById(R.id.btnMakeOffer).setOnClickListener(v -> showOfferDialog());
    }
    private void showOfferDialog() {
        EditText input = new EditText(this); input.setHint("Amount in RM"); input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL); input.setSingleLine(true);
        new AlertDialog.Builder(this).setTitle("Make an offer").setMessage("Send an offer to " + product.seller).setView(input).setNegativeButton("Cancel", null).setPositiveButton("Send offer", (d,w) -> { String amount=input.getText().toString().trim(); if (amount.isEmpty()) { Toast.makeText(this,"Enter an offer amount",Toast.LENGTH_SHORT).show(); return; } AppDataStore.addTransaction(this, product.id, product.title, "RM " + amount); Toast.makeText(this,"Offer sent",Toast.LENGTH_SHORT).show(); }).show();
    }
}
