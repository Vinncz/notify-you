package com.vinapp.notifyyou.data_access_and_storage.database_access;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vinapp.notifyyou.R;
import android.content.Context;

import com.vinapp.notifyyou.application_starter.AppContextHelper;
import com.vinapp.notifyyou.data_access_and_storage.data_access_objects.TileItemDAO;
import com.vinapp.notifyyou.models.TileItem;

@Database(entities = {TileItem.class}, version = 1, exportSchema = false)
public abstract class TileItemDatabase extends RoomDatabase {

    private static TileItemDatabase instance;
    public abstract TileItemDAO dao();

    public static synchronized TileItemDatabase getInstance () {
        if (instance == null) initializeInstance();

        return instance;
    }

    public static synchronized void initializeInstance () {
        Context appContext = AppContextHelper.getAppContext();
        String databaseName = appContext.getResources().getString(R.string.database_name);

        instance = Room.databaseBuilder (
                        appContext,
                        TileItemDatabase.class,
                        databaseName
                ) .fallbackToDestructiveMigration()
                  .build();
    }

}
