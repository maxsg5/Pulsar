package com.example.pulsar;

import static java.lang.System.*;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {

    //private List<AlarmModel> alarmModelList;
    public static final String ALARM_TABLE = "'ALARM_TABLE'";
    public static final String COLUMN_ALARM_ID="'ALARM'";
    public static final String COLUMN_ALARM_TIME = "'ALARM_TIME'";
    public static final String COLUMN_TARGET_BPM = "'TARGET_BPM'";
    public static final String COLUMN_ALARM_SOUND = "'ALARM_SOUND'";
    public static final String COLUMN_SNOOZE_PERIOD="'SNOOZE_PERIOD'";

    public static final String DEVICE_TABLE ="'DEVICE_TABLE'";
    public static final String COLUMN_DEVICE_ID="'DEVICE_ID'";
    public static final String COLUMN_DEVICE_NAME ="'DEVICE_NAME'";
    public static final String COLUMN_MAC_ADDRESS="'MAC_ADDRESS'";



    public DataBaseHelper(@Nullable Context context) { super(context, "pulsar.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Availability availability = new Availability();
        String createTableStatement = "CREATE TABLE " + ALARM_TABLE + " (" + COLUMN_ALARM_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ALARM_TIME + " text, "
                + COLUMN_TARGET_BPM + " text, "
                + COLUMN_ALARM_SOUND + " text, "
                + COLUMN_SNOOZE_PERIOD + " text " + " )";
        db.execSQL(createTableStatement);
        // availability.maketable(db);

        //String createTableStatement = ""
        //db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean StartDatabase(){
        AlarmModel employeeModel;

        //AlarmModel = new AlarmModel(123, "12:30", "120", "Taylor Swift", "10" );

        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        return true;


    }


    public boolean addAlarm(AlarmModel alarmModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        String check =alarmModel.getAlarm_time();
        out.print("check is "+ check);


        cv.put(COLUMN_ALARM_ID, alarmModel.getAlarm_id());
        cv.put(COLUMN_ALARM_TIME, alarmModel.getAlarm_time());
        cv.put(COLUMN_TARGET_BPM, alarmModel.getTarget_bpm());
        cv.put(COLUMN_ALARM_SOUND, alarmModel.getAlarm_sound());
        cv.put(COLUMN_SNOOZE_PERIOD, alarmModel.getSnooze_period());

        //String check;
        //String querystring = "SELECT * FROM" +ALARM_TABLE+ "WHERE" + COLUMN_ALARM_TIME+"="+alarmModel.getAlarm_time();
        //check = String.valueOf(db.rawQuery(querystring,null));
        //out.println(" the value is  "+ check);

        long insert = db.insertOrThrow(ALARM_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addDevice(bluetoothModel bluetoothModel){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DEVICE_ID, bluetoothModel.getDevice_id());
        cv.put(COLUMN_DEVICE_NAME, bluetoothModel.getDevice_id());
        cv.put(COLUMN_MAC_ADDRESS, bluetoothModel.getMac_address());

        long insert = db.insertOrThrow(ALARM_TABLE, null, cv);
        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }

    }

    public void  dropTables(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS "+ALARM_TABLE);

        db.close();
    }

    public boolean checkAlarmTime(String time){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = "SELECT * FROM " +ALARM_TABLE+ " WHERE " + COLUMN_ALARM_TIME+"=?";
        String query = "SELECT * FROM ALARM_TABLE WHERE ALARM_TIME =  ?";
        Cursor cursor = db.rawQuery(query, new String[]{time});
        if(cursor.getCount() <= 0)
        {
            cursor.close();
            out.println("This is false!!");
            return false;
        }
        else
        {
            cursor.close();
            out.println(" this is true found in db");
            return true;
        }
    }

    public void make_alarm_table(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createTableStatement = "CREATE TABLE " + ALARM_TABLE + " (" + COLUMN_ALARM_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ALARM_TIME + " text, "
                + COLUMN_TARGET_BPM + " text, "
                + COLUMN_ALARM_SOUND + " text, "
                + COLUMN_SNOOZE_PERIOD + " text " + " )";
        db.execSQL(createTableStatement);
        db.close();
    }

    public void make_device_table(){

        SQLiteDatabase db = this.getWritableDatabase();
        String createTableStatement = "CREATE TABLE " + DEVICE_TABLE + " (" + COLUMN_DEVICE_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DEVICE_NAME + " text, "
                + COLUMN_MAC_ADDRESS + " text " + " )";
        db.execSQL(createTableStatement);
        db.close();

    }



    public void updateAlarm(AlarmModel alarmModel) {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ALARM_ID, alarmModel.getAlarm_id());
        cv.put(COLUMN_ALARM_TIME, alarmModel.getAlarm_time());
        cv.put(COLUMN_TARGET_BPM, alarmModel.getTarget_bpm());
        cv.put(COLUMN_ALARM_SOUND, alarmModel.getAlarm_sound());
        cv.put(COLUMN_SNOOZE_PERIOD, alarmModel.getSnooze_period());

        db.update(ALARM_TABLE, cv, "ALARM=?", new String[]{String.valueOf(alarmModel.getAlarm_id())});
        db.close();

    }

    public List<AlarmModel> get_all_alarms(){
        List<AlarmModel> returnlist = new ArrayList<>();
        //alarmModelList = new ArrayList<>();

        String queryString = "Select *FROM"+ALARM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {

            do {
                int alarmID = cursor.getInt(0);
                String alarmTime = cursor.getString(1);
                int targetBPM = cursor.getInt(2);
                String alarmSound = cursor.getString(3);
                int snoozePeriod = cursor.getInt(4);

                AlarmModel newAlarm = new AlarmModel(alarmID, alarmTime, targetBPM, alarmSound, snoozePeriod);
                out.println(newAlarm.toString());
                returnlist.add(newAlarm);

            } while (cursor.moveToNext());
        }
            cursor.close();
            db.close();
            return returnlist;
        }


        public void DeleteAlarm(int id){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(ALARM_TABLE, "ALARM=?", new String[]{String.valueOf(id)});
            db.close();
        }



    }






