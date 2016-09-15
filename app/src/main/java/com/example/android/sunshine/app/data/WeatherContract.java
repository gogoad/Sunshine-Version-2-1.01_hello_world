package com.example.android.sunshine.app.data;

import android.provider.BaseColumns;

/**
 * Created by jxa on 2016/9/15.
 */
public class WeatherContract {

    //定义一张有关地点信息的表
    public static final class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME="location";

        public static final String COLUMN_LOCTION_SETTING="loction_setting";
        public static final String COLUMN_CITY_NAME="city";
    }

    public static final class WeatherEntry implements BaseColumns {
        public static final String TABLE_NAME="weather";
        //位置信息表中的外键
        public static final String COLUMN_LOC_KEY="location_id";
        //日期
        public static final String COLUMN_DATE="date";
        //天气id
        public static final String COLUMN_WEATHER_ID="weather_id";
        //温度
        public static final String COLUMN_TEMPERATURE="temperature";
        //天气
        public static final String COLUMN_WEATHER="weather";
        //星期
        public static final String COLUMN_WEEK="week";
        //风速
        public static final String COLUMN_WIND="wind";
    }
 }
