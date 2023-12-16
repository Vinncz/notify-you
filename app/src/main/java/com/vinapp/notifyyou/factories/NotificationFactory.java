package com.vinapp.notifyyou.factories;

import android.app.Notification;

import androidx.core.app.NotificationCompat;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.GlobalValueHolder;

public class NotificationFactory {

    public Notification createPersistent (String _title, String _body) {
        return new NotificationCompat.Builder(
                GlobalValueHolder.getAppContext(),
                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_pin_unpin_operation)
        )
                .setOngoing(true)
                .setContentTitle(_title)
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

    public Notification createAlarm (String _title, String _body) {
        return new NotificationCompat.Builder(
                GlobalValueHolder.getAppContext(),
                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_tileitem_alarm)
        )
                .setContentTitle("Time to do " + _title.toLowerCase() + "!")
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();
    }

}
