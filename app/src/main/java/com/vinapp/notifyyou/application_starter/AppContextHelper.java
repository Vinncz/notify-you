package com.vinapp.notifyyou.application_starter;

import android.content.Context;

public class AppContextHelper {

    private static Context appContext;

    public static void setAppContext(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

}
