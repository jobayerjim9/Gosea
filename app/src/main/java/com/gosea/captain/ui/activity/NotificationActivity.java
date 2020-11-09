package com.gosea.captain.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gosea.captain.R;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        String tittle = getIntent().getStringExtra("tittle");
        String body = getIntent().getStringExtra("body");
        TextView notificationTittle = findViewById(R.id.notificationTittle);
        TextView notificationBody = findViewById(R.id.notificationBody);
        notificationTittle.setText(tittle);
        notificationBody.setText(body);
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NotificationActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NotificationActivity.this, MainActivity.class));
        finish();
    }
}