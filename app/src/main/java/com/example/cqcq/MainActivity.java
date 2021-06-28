package com.example.cqcq;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch1;

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String SWITCH1 = "switch1";

    private boolean switchOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switch1 = findViewById(R.id.switch1);

        intent = new Intent(MainActivity.this, MyService.class);

        loadData();
        switch1.setChecked(switchOnOff);

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                TextView t = findViewById(R.id.editTextNumber);
                String min = t.getText().toString();
                if(min.matches("")) {
                    Toast.makeText(getApplicationContext(), "Text field can't be empty", Toast.LENGTH_SHORT).show();
                    switch1.setChecked(false);
                } else {
                    intent.putExtra("minutes", min);
                    saveData(true);
                    Toast.makeText(getApplicationContext(), "Set to repeat after " + min + " minutes", Toast.LENGTH_SHORT).show();

                    ContextCompat.startForegroundService(MainActivity.this, intent);
                }
            } else {
                saveData(false);
                Toast.makeText(getApplicationContext(),"Stopped alarm", Toast.LENGTH_SHORT).show();
                stopService(intent);
            }
        });
    }

    public void saveData(boolean myBool) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, myBool);
        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        switchOnOff = sharedPreferences.getBoolean(SWITCH1,false);
    }
}



