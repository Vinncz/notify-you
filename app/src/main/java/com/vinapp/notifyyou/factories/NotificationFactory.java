package com.vinapp.notifyyou.factories;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.vinapp.notifyyou.R;
import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.models.TileItem;
import com.vinapp.notifyyou.utilities.broadcast_receivers.AlarmBroadcastReceiver;
import com.vinapp.notifyyou.views.activities.EditTileitemActivity;
import com.vinapp.notifyyou.views.activities.HomeActivity;

public class NotificationFactory {

    public Notification createPersistent (TileItem _object) {
        Bundle dataBundle = new Bundle();
        dataBundle.putInt("tileItemId", _object.getId());

        Intent backIntent = new Intent(GlobalValueHolder.getAppContext(), HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent i = new Intent(GlobalValueHolder.getAppContext(), EditTileitemActivity.class).putExtras(dataBundle);
        PendingIntent pi = PendingIntent.getActivities(GlobalValueHolder.getAppContext(), _object.getId(), new Intent[] {backIntent, i}, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(
                GlobalValueHolder.getAppContext(),
                GlobalValueHolder.getAppContext().getResources().getString(R.string.notification_channel_id_for_pin_unpin_operation)
            )
                .setAutoCancel(false)
                .setContentIntent(pi)
                .setColorized(true)
                .setColor(GlobalValueHolder.getAppContext().getColor(R.color.colorPrimary))
                .setOngoing(true)
                .setContentTitle(_object.getTitle())
                .setContentText(_object.getBody())
                .setSmallIcon(R.drawable.ic_bell_fullcolor)
                .build();
    }

    public Notification createAlarm (String _title, String _body, Integer _id) {
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
                .setContentTitle("It's time to " + _title.toLowerCase() + "!")
                .setContentText(_body)
                .setSmallIcon(R.drawable.ic_bell_fullcolor)
                .build();
    }

}
