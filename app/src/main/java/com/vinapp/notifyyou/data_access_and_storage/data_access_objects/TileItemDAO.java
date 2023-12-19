package com.vinapp.notifyyou.data_access_and_storage.data_access_objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.vinapp.notifyyou.models.TileItem;

import java.util.List;

@Dao
public interface TileItemDAO {

    @Query("SELECT * FROM tile_items ORDER BY createdAt DESC LIMIT 1")
    LiveData<TileItem> getLatest ();

    @Query("SELECT * FROM tile_items WHERE id = :id LIMIT 1")
    LiveData<TileItem> get (Integer id);

    @Query("SELECT * FROM tile_items ORDER BY modifiedAt")
    LiveData<List<TileItem>> getAll ();

    @Query("SELECT * FROM tile_items WHERE isPinned = 1")
    LiveData<List<TileItem>> getPinned ();

    @Query("SELECT * FROM tile_items WHERE isPinned = 0")
    LiveData<List<TileItem>> getUnpinned ();

    @Insert
    long insert (TileItem ti);

    @Update
    void update (TileItem ti);

    @Delete
    void delete (TileItem ti);

}
