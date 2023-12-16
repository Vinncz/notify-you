package com.vinapp.notifyyou.controllers;

import android.app.Notification;
import android.app.NotificationManager;

import com.vinapp.notifyyou.application_starter.NotifyYou;
import com.vinapp.notifyyou.models.TileItem;

public class TileItemController {

    public class ValidationReturnStatement {
        public boolean isSuccesful;
        public String message;
        public ValidationReturnStatement (boolean _isSuccesful, String _message) {
            this.isSuccesful = _isSuccesful;
            this.message = _message;
        }
    }

    private final static NotificationManager notifManager = NotifyYou.getAppNotificationManager();

    public ValidationReturnStatement validate (TileItem _ti) {
        if ( ValidationController.isBlank(_ti.getTitle()) && ValidationController.isBlank(_ti.getBody()) ) {
            return new ValidationReturnStatement(false, "At least write something!");

        } else if ( ValidationController.isBlank(_ti.getTitle()) ) {
            return new ValidationReturnStatement(false, "Title is required!");
        }

        return new ValidationReturnStatement(true, "");
    }

    public void notify (Integer _notifId, Notification _notif) {
        notifManager.notify(_notifId, _notif);
    }

    public void cancel (Integer _notifId) {
        notifManager.cancel(_notifId);
    }

    public void activateAlarm (TileItem _object) {

    }

}
