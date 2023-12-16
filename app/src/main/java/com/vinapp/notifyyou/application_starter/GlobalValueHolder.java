package com.vinapp.notifyyou.application_starter;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;

public class GlobalValueHolder {

    private static Context appContext;
    private static NotificationManager appNotifManager;
    private static AlarmManager appAlarmManager;

    public static void setAppContext(Context _context) {
        appContext = _context.getApplicationContext();
    }

    public static void setAppNotificationManager (NotificationManager _nm) {
        appNotifManager = _nm;
    }

    public static void setAppAlarmManager (AlarmManager _am) {
        appAlarmManager = _am;
    }

    public static Context getAppContext() {
        return appContext;
    }
    public static NotificationManager getAppNotificationManager () {
        return appNotifManager;
    }
    public static AlarmManager getAppAlarmManager () { return appAlarmManager; }

}
