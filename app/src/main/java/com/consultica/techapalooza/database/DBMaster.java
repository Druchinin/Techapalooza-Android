package com.consultica.techapalooza.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.consultica.techapalooza.model.Band;
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

    public long insertSchedule(Schedule s) {
        ContentValues cv = new ContentValues();
        cv.put(DBCreator.Schedule.SCHEDULE_ID, s.getId());
        cv.put(DBCreator.Schedule.SCHEDULE_START_AT, s.getStarts_at());
        cv.put(DBCreator.Schedule.SCHEDULE_NAME, s.getName());
        cv.put(DBCreator.Schedule.SCHEDULE_AMOUNT_OF_BAND, s.getAmountOfBand());
        cv.put(DBCreator.Schedule.SCHEDULE_BAND_ID, s.getBand_Id());
        cv.put(DBCreator.Schedule.SCHEDULE_BAND_NAME, s.getBand_name());

        return database.insert(DBCreator.Schedule.TABLE_SCHEDULE, null, cv);
    }

    public List<Schedule> getAllSchedule() {
        String query = "SELECT "
                + DBCreator.Schedule.SCHEDULE_ID + ", "
                + DBCreator.Schedule.SCHEDULE_START_AT + ", "
                + DBCreator.Schedule.SCHEDULE_NAME + ", "
                + DBCreator.Schedule.SCHEDULE_AMOUNT_OF_BAND + ", "
                + DBCreator.Schedule.SCHEDULE_BAND_ID + ", "
                + DBCreator.Schedule.SCHEDULE_BAND_NAME
                + " FROM " + DBCreator.Schedule.TABLE_SCHEDULE + ";";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        List<Schedule> list = new ArrayList<>();

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

    public long insertBand(Band band) {
        ContentValues cv = new ContentValues();
        cv.put(DBCreator.Bands.BANDS_ID, band.getId());
        cv.put(DBCreator.Bands.BANDS_NAME, band.getName());
        cv.put(DBCreator.Bands.BANDS_LOGO, band.getLogo());
        cv.put(DBCreator.Bands.BANDS_DESCRIPTION, band.getDescription());
        cv.put(DBCreator.Bands.BANDS_DATE, band.getDate());

        return database.insert(DBCreator.Bands.TABLE_BANDS, null, cv);
    }

    public List<Band> getAllBands() {
        String query = "SELECT "
                + DBCreator.Bands.BANDS_ID+ ", "
                + DBCreator.Bands.BANDS_NAME + ", "
                + DBCreator.Bands.BANDS_LOGO + ", "
                + DBCreator.Bands.BANDS_DESCRIPTION + ", "
                + DBCreator.Bands.BANDS_DATE
                + " FROM " + DBCreator.Bands.TABLE_BANDS + ";";

        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        List<Band> list = new ArrayList<>();

        while (!cursor.isAfterLast()){
            Band band = new Band();
            band.setId(cursor.getString(0));
            band.setName(cursor.getString(1));
            band.setLogo(cursor.getString(2));
            band.setDescription(cursor.getString(3));
            band.setDate(cursor.getString(4));

            list.add(band);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    public Band getBand(String id){
        String query = "SELECT "
                + DBCreator.Bands.BANDS_ID+ ", "
                + DBCreator.Bands.BANDS_NAME + ", "
                + DBCreator.Bands.BANDS_LOGO + ", "
                + DBCreator.Bands.BANDS_DESCRIPTION + ", "
                + DBCreator.Bands.BANDS_DATE
                + " FROM " + DBCreator.Bands.TABLE_BANDS
                + " WHERE "+ DBCreator.Bands.BANDS_ID
                + " = '" + id + "';";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Band band = new Band();
        if (!cursor.isAfterLast()){
            band.setId(cursor.getString(0));
            band.setName(cursor.getString(1));
            band.setLogo(cursor.getString(2));
            band.setDescription(cursor.getString(3));
            band.setDate(cursor.getString(4));
        }
        cursor.close();

        return band;
    }

    public void clearBands() {
        database.execSQL("DROP TABLE IF EXISTS " + DBCreator.Bands.TABLE_BANDS + ";");
        database.execSQL(DBCreator.SCRIPT_CREATE_TBL_BANDS);
    }

}
