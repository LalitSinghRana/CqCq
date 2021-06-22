package com.example.cqcq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        intent = new Intent(this, MyService.class);
    }

    public void startForeSer(View view) {
        TextView t = findViewById(R.id.editTextNumber);

        String min = t.getText().toString();
        Toast.makeText(getApplicationContext(), "Set to " + min, Toast.LENGTH_LONG).show();

        intent = new Intent(this, MyService.class);
        intent.putExtra("minutes", min);
//        intent.putExtra("hours", hour)


        ContextCompat.startForegroundService(this, intent);
    }

    public void stopForeSer(View view) {
        stopService(intent);
    }
}