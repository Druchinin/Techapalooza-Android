package com.consultica.techapalooza.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBCreator extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_techapalooza";
    public static final int DB_VERSION = 7;

    static String SCRIPT_CREATE_TBL_SCHEDULE = " CREATE TABLE " + Schedule.TABLE_SCHEDULE + " ("
            + Schedule._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Schedule.SCHEDULE_ID + " TEXT, "
            + Schedule.SCHEDULE_START_AT + " TEXT, "
            + Schedule.SCHEDULE_ENDS_AT + " TEXT, "
            + Schedule.SCHEDULE_NAME + " TEXT, "
            + Schedule.SCHEDULE_AMOUNT_OF_BAND + " INTEGER, "
            + Schedule.SCHEDULE_BAND_ID + " TEXT, "
            + Schedule.SCHEDULE_BAND_NAME + " TEXT" + ");";

    static String SCRIPT_CREATE_TBL_BANDS = " CREATE TABLE " + Bands.TABLE_BANDS + " ("
            + Bands._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Bands.BANDS_ID + " TEXT, "
            + Bands.BANDS_NAME + " TEXT, "
            + Bands.BANDS_LOGO + " TEXT, "
            + Bands.BANDS_DESCRIPTION + " INTEGER, "
            + Bands.BANDS_DATE + " TEXT" + ");";

    static String SCRIPT_CREATE_TBL_NEWS = " CREATE TABLE " + News.TABLE_NEWS + " ("
            + News._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + News.NEWS_ID + " TEXT, "
            + News.NEWS_TITLE + " TEXT, "
            + News.NEWS_CONTENT + " TEXT, "
            + News.NEWS_DATE + " TEXT, "
            + News.NEWS_IMAGE + " TEXT" + ");";

    static String SCRIPT_CREATE_TBL_TICKETS = " CREATE TABLE " + Ticket.TABLE_TICKETS + " ("
            + Ticket._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Ticket.TICKET_ID + " TEXT, "
            + Ticket.TICKET_id + " INTEGER, "
            + Ticket.TICKET_CODE + " TEXT" + ");";


    public DBCreator(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static class Ticket implements BaseColumns {
        public static final String TABLE_TICKETS = "t_tickets";
        public static final String TICKET_ID = "ticket_id";
        public static final String TICKET_CODE = "ticket_code";
        public static final String TICKET_id = "ticketId";
    }

    public static class News implements BaseColumns {
        public static final String TABLE_NEWS = "t_news";
        public static final String NEWS_ID = "news_id";
        public static final String NEWS_TITLE = "news_title";
        public static final String NEWS_CONTENT = "news_content";
        public static final String NEWS_DATE = "news_date";
        public static final String NEWS_IMAGE = "news_image";
    }

    public static class Schedule implements BaseColumns {
        public static final String TABLE_SCHEDULE = "t_schedule";
        public static final String SCHEDULE_ID = "schedule_id";
        public static final String SCHEDULE_NAME = "schedule_name";
        public static final String SCHEDULE_START_AT = "schedule_start_at";
        public static final String SCHEDULE_ENDS_AT = "schedule_ands_at";
        public static final String SCHEDULE_AMOUNT_OF_BAND = "schedule_has_band";
        public static final String SCHEDULE_BAND_ID = "schedule_band_id";
        public static final String SCHEDULE_BAND_NAME = "schedule_band_name";
    }

    public static class Bands implements BaseColumns {
        public static final String TABLE_BANDS = "t_bands";
        public static final String BANDS_ID = "band_id";
        public static final String BANDS_NAME = "band_name";
        public static final String BANDS_LOGO = "band_logo";
        public static final String BANDS_DESCRIPTION = "band_description";
        public static final String BANDS_DATE = "band_date";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_CREATE_TBL_SCHEDULE);
        db.execSQL(SCRIPT_CREATE_TBL_BANDS);
        db.execSQL(SCRIPT_CREATE_TBL_NEWS);
        db.execSQL(SCRIPT_CREATE_TBL_TICKETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Schedule.TABLE_SCHEDULE + ";");
        db.execSQL(SCRIPT_CREATE_TBL_SCHEDULE);

        db.execSQL("DROP TABLE IF EXISTS " + Bands.TABLE_BANDS + ";");
        db.execSQL(SCRIPT_CREATE_TBL_BANDS);

        db.execSQL("DROP TABLE IF EXISTS " + News.TABLE_NEWS + ";");
        db.execSQL(SCRIPT_CREATE_TBL_NEWS);

        db.execSQL("DROP TABLE IF EXISTS " + Ticket.TABLE_TICKETS+ ";");
        db.execSQL(SCRIPT_CREATE_TBL_TICKETS);
    }
}
