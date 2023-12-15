package com.vinapp.notifyyou.factories;

import android.app.Notification;

import androidx.core.app.NotificationCompat;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.AppContextHelper;

public class NotificationFactory {

    public static Notification createPresistent (String _title, String _body) {
        return new NotificationCompat.Builder(AppContextHelper.getAppContext(), String.valueOf(R.string.notification_channel_id_for_pin_unpin_operation))
                .setOngoing(true)
                .setContentTitle(_title)
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

}
