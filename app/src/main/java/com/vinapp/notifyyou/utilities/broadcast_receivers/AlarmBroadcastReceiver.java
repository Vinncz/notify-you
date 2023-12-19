package com.vinapp.notifyyou.utilities.broadcast_receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.vinapp.notifyyou.application_starter.GlobalValueHolder;
import com.vinapp.notifyyou.data_access_and_storage.view_models.TileItemViewModel;
import com.vinapp.notifyyou.factories.NotificationFactory;
import com.vinapp.notifyyou.models.TileItem;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    private static AlarmBroadcastReceiver instance;

    public static synchronized AlarmBroadcastReceiver getInstance () {
        if (instance == null) initializeInstance();

        return instance;
    }

    public static void initializeInstance () {
        instance = new AlarmBroadcastReceiver();
    }

    private AlarmManager appAlarmManager = GlobalValueHolder.getAppAlarmManager();
    private NotificationManager appNotifManager = GlobalValueHolder.getAppNotificationManager();
    private TileItemViewModel vm = GlobalValueHolder.getAppMainActivityInstance().getViewModel();

    @Override
    public void onReceive (Context context, Intent intent) {
        if ( intent.getAction().equals("DISMISS_NOTIFICATION") ) {
            int id = intent.getIntExtra("idForAlarm", -1);
            Log.d("onReceive", "id sent: " + id);

            appNotifManager.cancel(id);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<LiveData<TileItem>> future = ( Future<LiveData<TileItem>> ) executor.submit(() -> {
                vm.getById(id);
            });

            try {
                LiveData<TileItem> ldti = future.get();
                TileItem ti = ldti.getValue();
                ti.setAlarmIsActive(false);
                vm.update(ti);

            } catch ( InterruptedException | ExecutionException e) {}

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
