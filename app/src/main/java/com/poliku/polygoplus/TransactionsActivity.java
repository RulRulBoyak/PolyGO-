package com.poliku.polygoplus;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.poliku.polygoplus.data.AppDataStore;
public class TransactionsActivity extends AppCompatActivity { @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_transactions);findViewById(R.id.btnBack).setOnClickListener(v->finish());LinearLayout list=findViewById(R.id.transactionList);for(String[] t:AppDataStore.getTransactions(this)){MaterialCardView card=new MaterialCardView(this);card.setRadius(16);card.setCardElevation(0);card.setUseCompatPadding(true);TextView text=new TextView(this);text.setPadding(20,18,20,18);text.setText(t[0]+"\n"+t[1]+"  •  "+t[2]);text.setTextSize(15);card.addView(text);list.addView(card);}}}
