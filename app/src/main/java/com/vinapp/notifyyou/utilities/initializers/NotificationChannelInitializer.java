package com.vinapp.notifyyou.utilities.initializers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.media.AudioAttributes;
import android.net.Uri;

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

        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ GlobalValueHolder.getAppContext().getPackageName() + "/" + R.raw.never_gonna_give_you_up);
        NotificationChannel channelForAlarmOperation = new NotificationChannel(
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_tileitem_alarm),
                                                                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_name_for_tileitem_alarm),
                                                                NotificationManager.IMPORTANCE_HIGH
                                                            );
        AudioAttributes attr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build();
        channelForAlarmOperation.enableLights(true);
        channelForAlarmOperation.enableVibration(true);
        channelForAlarmOperation.setSound(sound, attr);

        _notifManager.createNotificationChannel(channelForAlarmOperation);
    }

}
