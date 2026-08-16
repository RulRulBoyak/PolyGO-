package com.poliku.polygoplus;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import com.poliku.polygoplus.data.AppDataStore;
public class NotificationsActivity extends AppCompatActivity { @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_notifications);findViewById(R.id.btnBack).setOnClickListener(v->finish());LinearLayout list=findViewById(R.id.notificationList);for(AppDataStore.NotificationRecord n:AppDataStore.getNotifications(this)){MaterialCardView card=new MaterialCardView(this);card.setRadius(16);card.setCardElevation(0);card.setUseCompatPadding(true);TextView text=new TextView(this);text.setPadding(20,18,20,18);text.setText(n.title+"\n"+n.body);text.setTextSize(15);card.addView(text);list.addView(card);}}}
