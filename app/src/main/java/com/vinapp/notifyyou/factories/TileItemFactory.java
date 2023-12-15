package com.vinapp.notifyyou.factories;

import com.vinapp.notifyyou.models.TileItem;

import java.sql.Timestamp;

public class TileItemFactory {

    public static TileItem make (String _title, String _body, String _alarmTime, Boolean _pinnedOnCreation, Boolean _alarmActiveOnCreation) {
        TileItem returnObject = new TileItem();

        returnObject.setTitle         ( _title );
        returnObject.setBody          ( _body );
        returnObject.setPinned        ( _pinnedOnCreation );
        returnObject.setAlarmTime     ( _alarmTime );
        returnObject.setAlarmIsActive ( _alarmActiveOnCreation );
        returnObject.setCreatedAt     ( new Timestamp(System.currentTimeMillis()) );
        returnObject.setModifiedAt    ( returnObject.getCreatedAt() );

        return returnObject;
    }

}
