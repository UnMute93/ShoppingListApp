package com.example.unmute.shoppinglistapp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ShoppingList {
    private int id;
    private @NonNull String title = "";
    private boolean setAlarm;
    private @Nullable Timestamp alarmDate;
    private Timestamp createdDate;
    private List<ListItem> listItems;

    public ShoppingList() {

    }
    public ShoppingList(int id, @NonNull String title, boolean setAlarm, @Nullable Timestamp alarmDate) {
        this.id = id;
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.listItems = new ArrayList<ListItem>();
    }
    public ShoppingList(int id, @NonNull String title, boolean setAlarm, @Nullable Timestamp alarmDate, Timestamp createdDate) {
        this.id = id;
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = createdDate;
        this.listItems = new ArrayList<ListItem>();
    }
    public ShoppingList(@NonNull String title, boolean setAlarm, @Nullable Timestamp alarmDate) {
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.listItems = new ArrayList<ListItem>();
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }
    public @NonNull String getTitle() {
        return title;
    }

    public void setSetAlarm(boolean setAlarm) {
        this.setAlarm = setAlarm;
    }
    public boolean getSetAlarm() {
        return setAlarm;
    }

    public void setAlarmDate(@Nullable Timestamp alarmDate) {
        this.alarmDate = alarmDate;
    }
    public @Nullable Timestamp getAlarmDate() {
        return alarmDate;
    }
    public @Nullable Long getAlarmDateInMillis() {
        try {
            return alarmDate.getTime();
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    public long getCreatedDateInMillis() {
        return createdDate.getTime();
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }
    public void addListItem(ListItem li) {
        listItems.add(li);
    }
    public List<ListItem> getAllListItems() {
        return listItems;
    }
}
