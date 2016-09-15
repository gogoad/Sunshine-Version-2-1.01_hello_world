package com.example.android.sunshine.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jxa on 2016/9/15.
 */
public class WeatherDBHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME="weather.db";
    private static int DATABASE_VERSION = 1;


    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + WeatherContract.LocationEntry.TABLE_NAME +"(" +
                WeatherContract.LocationEntry._ID+" INTEGER PRIMARY KEY,"+
                WeatherContract.LocationEntry.COLUMN_LOCTION_SETTING+" TEXT UNIQUE NOT NULL,"+
                WeatherContract.LocationEntry.COLUMN_CITY_NAME+" TEXT NOT NULL"+")";

        final String SQL_CREATE_WEATHER_TABLE = " CREATE TABLE " + WeatherContract.WeatherEntry.TABLE_NAME +"(" +
                WeatherContract.WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL,"+
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL,"+
                WeatherContract.WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL,"+
                WeatherContract.WeatherEntry.COLUMN_TEMPERATURE+" REAL NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_WEATHER + " TEXT NOT NULL,"+
                WeatherContract.WeatherEntry.COLUMN_WEEK+" TEXT NOT NULL," +
                WeatherContract.WeatherEntry.COLUMN_WIND+" REAL NOT NULL,"+
                //设置位置信息列作为位置信息表的外键
                " FOREIGN KEY (" + WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                WeatherContract.LocationEntry.TABLE_NAME + " (" + WeatherContract.LocationEntry._ID + "), " +
                " UNIQUE (" + WeatherContract.WeatherEntry.COLUMN_DATE + ", " +
                WeatherContract.WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";
                db.execSQL(SQL_CREATE_LOCATION_TABLE);
                db.execSQL(SQL_CREATE_WEATHER_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS"+ WeatherContract.WeatherEntry.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS"+ WeatherContract.LocationEntry.TABLE_NAME);
            onCreate(db);
    }
}
