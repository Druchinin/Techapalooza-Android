package com.consultica.techapalooza.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.consultica.techapalooza.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class DBMaster {

    private SQLiteDatabase database;
    private DBCreator dbCreator;

    private static DBMaster instance;

    private DBMaster(Context context) {
        dbCreator = new DBCreator(context);
        if (database == null || !database.isOpen()) {
            database = dbCreator.getWritableDatabase();
        }
    }

    public static DBMaster getInstance(Context context) {
        if (instance == null) {
            instance = new DBMaster(context);
        }
        return instance;
    }

    public long insertSchedule(String id, String start_at, String name, int amountOfBands, String band_id, String band_name) {
        ContentValues cv = new ContentValues();
        cv.put(DBCreator.Schedule.SCHEDULE_ID, id);
        cv.put(DBCreator.Schedule.SCHEDULE_START_AT, start_at);
        cv.put(DBCreator.Schedule.SCHEDULE_NAME, name);
        cv.put(DBCreator.Schedule.SCHEDULE_AMOUNT_OF_BAND, amountOfBands);
        cv.put(DBCreator.Schedule.SCHEDULE_BAND_ID, band_id);
        cv.put(DBCreator.Schedule.SCHEDULE_BAND_NAME, band_name);

        return database.insert(DBCreator.Schedule.TABLE_SCHEDULE, null, cv);
    }

    public List<Schedule> getAllSchedule() {
        String query = "SELECT " + DBCreator.Schedule.SCHEDULE_ID
                + ", " + DBCreator.Schedule.SCHEDULE_START_AT
                + ", " + DBCreator.Schedule.SCHEDULE_NAME
                + ", " + DBCreator.Schedule.SCHEDULE_AMOUNT_OF_BAND
                + ", " + DBCreator.Schedule.SCHEDULE_BAND_ID
                + ", " + DBCreator.Schedule.SCHEDULE_BAND_NAME
                + " FROM " + DBCreator.Schedule.TABLE_SCHEDULE + ";";
        Cursor cursor = database.rawQuery(query, null);

        List<Schedule> list = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Schedule schedule = new Schedule();

            schedule.setId(cursor.getString(0));
            schedule.setStarts_at(cursor.getString(1));
            schedule.setName(cursor.getString(2));
            schedule.setAmountOfBand(cursor.getInt(3));
            schedule.setBand_Id(cursor.getString(4));
            schedule.setBand_name(cursor.getString(5));


            list.add(schedule);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public void clearSchedule() {
        database.execSQL("DROP TABLE IF EXISTS " + DBCreator.Schedule.TABLE_SCHEDULE + ";");
        database.execSQL(DBCreator.SCRIPT_CREATE_TBL_SCHEDULE);
    }

}
