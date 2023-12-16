package com.vinapp.notifyyou.controllers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.service.notification.StatusBarNotification;

import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.factories.NotificationFactory;
import com.vinapp.notifyyou.models.TileItem;

import java.util.Calendar;

public class TileItemController extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");

        NotificationFactory nf = new NotificationFactory();
        Notification n = nf.createAlarm(title, body);
        notifManager.notify(id, n);
    }

    public class ValidationReturnStatement {
        public boolean isSuccessful;
        public String message;
        public ValidationReturnStatement (boolean _isSuccessful, String _message) {
            this.isSuccessful = _isSuccessful;
            this.message = _message;
        }
    }

    private final NotificationManager notifManager = GlobalValueHolder.getAppNotificationManager();

    public ValidationReturnStatement validate (TileItem _ti) {
        if ( ValidationController.isBlank(_ti.getTitle()) && ValidationController.isBlank(_ti.getBody()) ) {
            return new ValidationReturnStatement(false, "At least write something!");
        } else if ( ValidationController.isBlank(_ti.getTitle()) ) {
            return new ValidationReturnStatement(false, "Title is required!");
        }

        return new ValidationReturnStatement(true, "");
    }

    public boolean notificationIsCurrentlyDisplayed (TileItem _object) {
        StatusBarNotification [] notifs = notifManager.getActiveNotifications();
        for (StatusBarNotification notif : notifs) {
            if ( notif.getId() == _object.getId() )
                return true;
        }

        return false;
    }

    public void notify (TileItem _object) {
        Integer notifId = _object.getId();
        NotificationFactory nf = new NotificationFactory();
        Notification notif = nf.createPersistent(_object.getTitle(), _object.getBody());
        notifManager.notify(notifId, notif);
    }

    public void cancel (TileItem _object) {
        Integer notifId = _object.getId();
        notifManager.cancel(notifId);
    }

    public void activateAlarm (TileItem _object) {
        int hour = Integer.parseInt(_object.getAlarmTime().substring(0, 2));
        int min  = Integer.parseInt(_object.getAlarmTime().substring(3, 5));

        Calendar alarmTime = Calendar.getInstance();
        alarmTime.set(Calendar.HOUR_OF_DAY, hour);
        alarmTime.set(Calendar.MINUTE     , min);
        alarmTime.set(Calendar.SECOND     , 0);

        AlarmManager alarmManager = GlobalValueHolder.getAppAlarmManager();
        Context context = GlobalValueHolder.getAppContext();

        Intent i = new Intent(context, ((BroadcastReceiver) this).getClass());
        i.putExtra("id", _object.getId());
        i.putExtra("title", _object.getTitle());
        i.putExtra("body", _object.getBody());

        if (alarmTime.before(Calendar.getInstance())){
            alarmTime.add(Calendar.DATE, 1);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _object.getId(), i, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm (TileItem _object) {
        AlarmManager alarmManager = GlobalValueHolder.getAppAlarmManager();
        Context context = GlobalValueHolder.getAppContext();
        Intent i = new Intent(context, ((BroadcastReceiver) this).getClass());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, _object.getId(), i, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }

}
