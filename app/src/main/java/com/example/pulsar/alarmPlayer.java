package com.example.pulsar;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class alarmPlayer {
    private Context context;


    public void playAlarm(AlarmModel alarmModel){

        String alarm=alarmModel.getAlarm_sound();
        String audiofile;
        audiofile= "R.raw" +alarm;
        MediaPlayer music = MediaPlayer.create(context, Uri.parse(audiofile));
        music.setVolume(100,100);
        music.start();

    }

}
