package com.poliku.polygoplus;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.ProductCardAdapter;
public class SavedItemsActivity extends AppCompatActivity { @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_saved_items);findViewById(R.id.btnBack).setOnClickListener(v->finish());AppDataStore.initialize(this);java.util.List<AppDataStore.ProductRecord> items=AppDataStore.getFavorites(this);RecyclerView rv=findViewById(R.id.rvSaved);rv.setLayoutManager(new GridLayoutManager(this,2));rv.setAdapter(new ProductCardAdapter(items,(a,p)->{android.content.Intent i=new android.content.Intent(this,ProductDetailActivity.class);i.putExtra(ProductDetailActivity.EXTRA_LISTING_ID,p.id);startActivity(i);}));TextView empty=findViewById(R.id.tvEmpty);empty.setVisibility(items.isEmpty()?View.VISIBLE:View.GONE);}}
