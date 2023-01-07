package com.example.pulsar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextClock;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup UI elements to link them to code
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextClock clock = findViewById(R.id.Clock);

        //set progress bar to 0%
        progressBar.setProgress(0);
    }

    //onStart method
    @Override
    protected void onStart() {
        super.onStart();

        //call updateProgress method every 1000ms
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateProgressBar(10);
            }
        }, 0, 1000);

    }

    //update progress bar by 10% every 1 second
    public void updateProgressBar(int incrementAmount){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        int progress = progressBar.getProgress();
        progress += incrementAmount;
        progressBar.setProgress(progress);
    }
}