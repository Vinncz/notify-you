package com.vinapp.notifyyou.utilities.converters;

import androidx.room.TypeConverter;

import java.sql.Timestamp;

public class TimestampConverter {

    @TypeConverter
    public static Timestamp fromTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.getTime();
    }
}
