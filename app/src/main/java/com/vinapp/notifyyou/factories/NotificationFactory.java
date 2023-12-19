package com.vinapp.notifyyou.factories;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.utilities.broadcast_receivers.AlarmBroadcastReceiver;

public class NotificationFactory {

    public Notification createPersistent (String _title, String _body) {
        return new NotificationCompat.Builder(
                GlobalValueHolder.getAppContext(),
                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_pin_unpin_operation)
        )
                .setColorized(true)
                .setColor(GlobalValueHolder.getAppContext().getColor(R.color.colorPrimary))
                .setOngoing(true)
                .setContentTitle(_title)
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_bell_fullcolor)
                .build();
    }

    public Notification createAlarm (String _title, String _body, Integer _id) {
        Log.d("FactoryCreate", "Received id to be packed into Intent: " + _id);

        Context context = GlobalValueHolder.getAppContext();
        String channelId = context.getResources().getString(R.string.notification_channel_id_for_tileitem_alarm);

        Intent dismissIntent = new Intent(context, AlarmBroadcastReceiver.getInstance().getClass()).setAction("DISMISS_NOTIFICATION").putExtra("idForAlarm", _id);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(context, _id, dismissIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(
                context,
                channelId
        )
                .addAction(R.drawable.ic_bell_fullcolor, "Dismiss", dismissPendingIntent)
                .setOngoing(true)
                .setContentTitle("Time to do " + _title.toLowerCase() + "!")
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_bell_fullcolor)
                .build();
    }

}
