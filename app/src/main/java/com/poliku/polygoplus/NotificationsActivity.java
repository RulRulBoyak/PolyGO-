package com.poliku.polygoplus;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.NotificationAdapter;

public class NotificationsActivity extends AppCompatActivity {
    private NotificationAdapter adapter;
    private TextView empty;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        empty=findViewById(R.id.tvEmptyNotifications);
        RecyclerView list=findViewById(R.id.notificationList);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NotificationAdapter(notification -> {
            AppDataStore.markNotificationRead(this, notification.id);
            loadNotifications();
        });
        list.setAdapter(adapter);
        loadNotifications();
    }

    @Override protected void onResume() { super.onResume(); if(adapter!=null) loadNotifications(); }

    @Override protected void onStop() {
        super.onStop();
        AppDataStore.markNotificationsRead(this);
    }

    private void loadNotifications() {
        java.util.List<AppDataStore.NotificationRecord> items=AppDataStore.getNotifications(this);
        adapter.submit(items);
        empty.setVisibility(items.isEmpty()?View.VISIBLE:View.GONE);
    }
}
