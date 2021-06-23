package com.example.cqcq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private Switch switchButton;
//    String min = "5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchButton = (Switch)findViewById(R.id.switch1);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true) {
                    TextView t = findViewById(R.id.editTextNumber);
                    String min = t.getText().toString();

                    intent = new Intent(MainActivity.this, MyService.class);
                    intent.putExtra("minutes", min);

                    Toast.makeText(getApplicationContext(), "Set to " + min, Toast.LENGTH_LONG).show();
                    t.setText(min);

                    ContextCompat.startForegroundService(MainActivity.this, intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Off", Toast.LENGTH_SHORT).show();
                    stopService(intent);
                }
            }
        });

//        intent = new Intent(this, MyService.class);
    }

    /*public void startForeSer(View view) {
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
    }*/
}