package com.poliku.polygoplus;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.ProductCardAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY = "category";
    private final List<AppDataStore.ProductRecord> all = new ArrayList<>();
    private ProductCardAdapter adapter; private EditText search; private Spinner category; private TextView count, empty;
    @Override protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_search); AppDataStore.initialize(this); findViewById(R.id.btnBack).setOnClickListener(v->finish()); search=findViewById(R.id.etSearch); category=findViewById(R.id.spinnerCategory); count=findViewById(R.id.tvResultCount); empty=findViewById(R.id.tvEmptySearch); all.addAll(AppDataStore.getListings(this)); String[] categories={"All categories","Food","Drink","Tech","Electronics","Fashion","Books","Repair","Home","Services"}; category.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,categories)); String initial=getIntent().getStringExtra(EXTRA_CATEGORY); if(initial!=null){search.setText(initial); for(int i=0;i<categories.length;i++)if(categories[i].equalsIgnoreCase(initial))category.setSelection(i);} RecyclerView rv=findViewById(R.id.rvSearchResults); rv.setLayoutManager(new GridLayoutManager(this,2)); search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){} public void onTextChanged(CharSequence s,int st,int b,int c){filter();} public void afterTextChanged(Editable e){}}); category.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){filter();}public void onNothingSelected(android.widget.AdapterView<?> p){}}); filter(); }
    private void filter(){
        if (search == null || category == null) return;
        String q=search.getText().toString().trim().toLowerCase(Locale.ROOT);
        String cat=category.getSelectedItem()==null?"All categories":category.getSelectedItem().toString();
        List<AppDataStore.ProductRecord> filtered=new ArrayList<>();
        for(AppDataStore.ProductRecord p:all){
            boolean text=q.isEmpty()||p.title.toLowerCase(Locale.ROOT).contains(q)||p.seller.toLowerCase(Locale.ROOT).contains(q)||p.description.toLowerCase(Locale.ROOT).contains(q);
            boolean categoryMatch=cat.startsWith("All")||p.category.equalsIgnoreCase(cat);
            if(text&&categoryMatch)filtered.add(p);
        }
        adapter=new ProductCardAdapter(filtered,(a,p)->{
            android.content.Intent i=new android.content.Intent(this,ProductDetailActivity.class);
            i.putExtra(ProductDetailActivity.EXTRA_LISTING_ID,p.id);
            startActivity(i);
        });
        RecyclerView rv = findViewById(R.id.rvSearchResults);
        if (rv != null) rv.setAdapter(adapter);
        if (count != null) count.setText(filtered.size()+" listings");
        if (empty != null) empty.setVisibility(filtered.isEmpty()?View.VISIBLE:View.GONE);
    }
}
