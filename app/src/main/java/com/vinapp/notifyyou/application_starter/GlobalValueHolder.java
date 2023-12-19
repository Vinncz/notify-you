package com.vinapp.notifyyou.application_starter;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;

import com.vinapp.notifyyou.views.activities.HomeActivity;

public class GlobalValueHolder {

    private static Context appContext;
    private static NotificationManager appNotifManager;
    private static AlarmManager appAlarmManager;
    private static HomeActivity appMainActivityInstance;

    public static void setAppContext(Context _context) {
        appContext = _context.getApplicationContext();
    }

    public static void setAppNotificationManager (NotificationManager _nm) {
        appNotifManager = _nm;
    }

    public static void setAppAlarmManager (AlarmManager _am) {
        appAlarmManager = _am;
    }
    public static void setAppMainActivityInstance (HomeActivity _instance) { appMainActivityInstance = _instance; }

    public static Context getAppContext() {
        return appContext;
    }
    public static NotificationManager getAppNotificationManager () {
        return appNotifManager;
    }
    public static AlarmManager getAppAlarmManager () { return appAlarmManager; }
    public static HomeActivity getAppMainActivityInstance () { return appMainActivityInstance; }

}
