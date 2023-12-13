package com.vinapp.notifyyou.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.vinapp.notifyyou.utilities.converters.TimestampConverter;

import java.sql.Timestamp;

@Entity(tableName = "tile_items")
@TypeConverters({TimestampConverter.class})
public class TileItem {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String title, body, alarmTime;
    private Boolean isPinned = false, alarmIsActive = false;

    private Timestamp createdAt, modifiedAt;

    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getBody () {
        return body;
    }

    public void setBody (String body) {
        this.body = body;
    }

    public String getAlarmTime () {
        return alarmTime;
    }

    public void setAlarmTime (String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Boolean getPinned () {
        return isPinned;
    }

    public void setPinned (Boolean pinned) {
        isPinned = pinned;
    }

    public Boolean getAlarmIsActive () {
        return alarmIsActive;
    }

    public void setAlarmIsActive (Boolean alarmIsActive) {
        this.alarmIsActive = alarmIsActive;
    }

    public Timestamp getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getModifiedAt () {
        return modifiedAt;
    }

    public void setModifiedAt (Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
