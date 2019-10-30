package com.gps.gpsdome;

import android.content.Intent;
import android.os.Bundle;

import com.gps.gpsdome.service.GpsService;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent it = new Intent(this, GpsService.class);
        startService(it);
    }
}
