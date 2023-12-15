package com.vinapp.notifyyou.controllers;

import android.app.Notification;
import android.app.NotificationManager;

import com.vinapp.notifyyou.application_starter.NotifyYou;

public class TileItemController {

    private static NotificationManager notifManager = NotifyYou.getAppNotificationManager();

    public void notify (Integer _notifId, Notification _notif) {
        notifManager.notify(_notifId, _notif);
    }

    public void cancel (Integer _notifId) {
        notifManager.cancel(_notifId);
    }

}
