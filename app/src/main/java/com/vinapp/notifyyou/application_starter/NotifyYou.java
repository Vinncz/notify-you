package com.vinapp.notifyyou.application_starter;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

import com.vinapp.notifyyou.data_access_and_storage.database_access.TileItemDatabase;
import com.vinapp.notifyyou.utilities.initializers.NotificationChannelInitializer;

public class NotifyYou extends Application {

    private static NotificationManager appNotificationManager;

    @Override
    public void onCreate () {
        super.onCreate();
        AppContextHelper.setAppContext(getApplicationContext());
        appNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        TileItemDatabase.initializeInstance();
        NotificationChannelInitializer.initialize(getAppNotificationManager());
    }

    public static NotificationManager getAppNotificationManager () {
        return appNotificationManager;
    }

}
