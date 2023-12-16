package com.vinapp.notifyyou.application_starter;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlobalValueProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        Context context = getContext();
        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        GlobalValueHolder.setAppContext(context);
        GlobalValueHolder.setAppNotificationManager(notifManager);
        GlobalValueHolder.setAppAlarmManager(alarmManager);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType (@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert (@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update (@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
