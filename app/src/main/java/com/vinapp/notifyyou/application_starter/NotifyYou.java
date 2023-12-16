package com.vinapp.notifyyou.application_starter;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.vinapp.notifyyou.data_access_and_storage.database_access.TileItemDatabase;

public class NotifyYou extends Application {

    @Override
    public void onCreate () {
        super.onCreate();
        GlobalValueHolder.setAppContext(getApplicationContext());
        GlobalValueHolder.setAppNotificationManager((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        TileItemDatabase.initializeInstance();
    }

}
