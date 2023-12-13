package com.vinapp.notifyyou.utilities.initializers;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.vinapp.notifyyou.R;

public class NotificationChannelInitializer {

    public static void initialize ( NotificationManager _notifManager ) {
        NotificationChannel channelForPinUnpinOperation = new NotificationChannel(
                                                                String.valueOf(R.string.notification_channel_id_for_pin_unpin_operation),
                                                                String.valueOf(R.string.notification_channel_name_for_pin_unpin_operation),
                                                                NotificationManager.IMPORTANCE_HIGH
                                                            );
        _notifManager.createNotificationChannel(channelForPinUnpinOperation);

        NotificationChannel channelForAlarmOperation = new NotificationChannel(
                                                                String.valueOf(R.string.notification_channel_id_for_tileitem_alarm),
                                                                String.valueOf(R.string.notification_channel_name_for_tileitem_alarm),
                                                                NotificationManager.IMPORTANCE_HIGH
                                                            );
        _notifManager.createNotificationChannel(channelForAlarmOperation);
    }

}
