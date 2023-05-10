package com.example.pulsar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextClock;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup UI elements to link them to code
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is DeviceSelector fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DeviceSelectorFrag()).commit();

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    //check id
                    case R.id.bluetooth:
                        //if bluetooth is selected, open the bluetooth fragment
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DeviceSelectorFrag()).commit();

                }
                return true;
            }
        });
    }




}