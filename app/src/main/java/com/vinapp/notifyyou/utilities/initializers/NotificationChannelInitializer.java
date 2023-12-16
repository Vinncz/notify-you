package com.vinapp.notifyyou.utilities.initializers;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.GlobalValueHolder;

public class NotificationChannelInitializer {

    public void initialize ( NotificationManager _notifManager ) {
        NotificationChannel channelForPinUnpinOperation = new NotificationChannel(
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_pin_unpin_operation),
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_name_for_pin_unpin_operation),
                                                                NotificationManager.IMPORTANCE_HIGH
                                                            );
        _notifManager.createNotificationChannel(channelForPinUnpinOperation);

        NotificationChannel channelForAlarmOperation = new NotificationChannel(
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_tileitem_alarm),
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_name_for_tileitem_alarm),
                                                                NotificationManager.IMPORTANCE_HIGH
                                                            );
        _notifManager.createNotificationChannel(channelForAlarmOperation);
    }

}
