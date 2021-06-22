package com.example.cqcq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = new Intent(this, MyService.class);
    }

    public void startForeSer(View view) {
        ContextCompat.startForegroundService(this, intent);
    }

    public void stopForeSer(View view) {
        stopService(intent);
    }
}