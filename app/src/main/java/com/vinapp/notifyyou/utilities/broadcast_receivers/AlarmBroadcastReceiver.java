package com.vinapp.notifyyou.utilities.broadcast_receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.factories.NotificationFactory;
import com.vinapp.notifyyou.models.TileItem;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static AlarmBroadcastReceiver instance;

    public static synchronized AlarmBroadcastReceiver getInstance () {
        if (instance == null) initializeInstance();

        return instance;
    }

    public static void initializeInstance () {
        instance = new AlarmBroadcastReceiver();
    }

    private NotificationManager appNotifManager = GlobalValueHolder.getAppNotificationManager();
    private TileItemViewModel vm = GlobalValueHolder.getAppMainActivityInstance().getViewModel();

    @Override
    public void onReceive (Context context, Intent intent) {
        if ( intent.getAction().equals("DISMISS_NOTIFICATION") ) {
            int id = intent.getIntExtra("idForAlarm", -1);
            Log.d("onReceive", "id sent: " + id);

            appNotifManager.cancel(id);

            LiveData<TileItem> tileItemLiveData = vm.getById(id);

            LiveData<Boolean> updateTrigger = Transformations.map(tileItemLiveData, tileItem -> {
                if (tileItem != null && tileItem.getAlarmIsActive()) {
                    tileItem.setAlarmIsActive(false);
                    vm.update(tileItem);
                    return true;
                }

                return false;
            });

            updateTrigger.observe(GlobalValueHolder.getAppMainActivityInstance(), updateTriggered -> {
                //
            });


        } else if ( intent.getAction().equals("BROADCAST_ALARM") ) {
            int id = intent.getIntExtra("idForAlarm", -1);
            String title = intent.getStringExtra("title");
            String body = intent.getStringExtra("body");

            NotificationFactory nf = new NotificationFactory();
            Notification n = nf.createAlarm(title, body, id);

            n.extras.putInt("idForAlarm", id);
            appNotifManager.notify(id, n);
        }
    }
}
