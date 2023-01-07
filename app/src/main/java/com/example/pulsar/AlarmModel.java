package com.example.pulsar;

public class AlarmModel {

    private int alarm_id;
    private String alarm_time;
    private int target_bpm;
    private String alarm_sound;
    private int snooze_period;


    public AlarmModel(int alarm_id, String alarm_time, int target_bpm, String alarm_sound, int snooze_period) {
        this.alarm_id = alarm_id;
        this.alarm_time = alarm_time;
        this.target_bpm = target_bpm;
        this.alarm_sound = alarm_sound;
        this.snooze_period = snooze_period;
    }

    public int getAlarm_id() {
        return alarm_id;
    }
    public void setAlarm_id(int alarm_id){
        this.alarm_id=alarm_id;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_sound(String alarm_sound) {
        this.alarm_sound = alarm_sound;
    }

    public String getAlarm_sound() {
        return alarm_sound;
    }

    public int getSnooze_period() {
        return snooze_period;
    }

    public void setSnooze_period(int snooze_period) {
        this.snooze_period = snooze_period;
    }

    public int getTarget_bpm() {
        return target_bpm;
    }

    public void setTarget_bpm(int target_bpm) {
        this.target_bpm = target_bpm;
    }


}


