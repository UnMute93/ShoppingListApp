package com.example.unmute.shoppinglistapp;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.support.annotation.Nullable;

class ShoppingList {
    private int id;
    private String title = "";
    private boolean setAlarm;
    private @Nullable Timestamp alarmDate;
    private Timestamp createdDate;
    private List<ListItem> listItems;

    ShoppingList() {

    }
    public ShoppingList(int id, String title, boolean setAlarm, @Nullable Timestamp alarmDate) {
        this.id = id;
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.listItems = new ArrayList<>();
    }
    public ShoppingList(int id, String title, boolean setAlarm, @Nullable Timestamp alarmDate, Timestamp createdDate) {
        this.id = id;
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = createdDate;
        this.listItems = new ArrayList<>();
    }
    ShoppingList(String title, boolean setAlarm, @Nullable Timestamp alarmDate) {
        this.title = title;
        this.setAlarm = setAlarm;
        this.alarmDate = alarmDate;
        this.createdDate = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.listItems = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    void setSetAlarm(boolean setAlarm) {
        this.setAlarm = setAlarm;
    }
    boolean getSetAlarm() {
        return setAlarm;
    }

    void setAlarmDate(@Nullable Timestamp alarmDate) {
        this.alarmDate = alarmDate;
    }
    public @Nullable Timestamp getAlarmDate() {
        return alarmDate;
    }
    @Nullable Long getAlarmDateInMillis() {
        if (alarmDate != null)
            return alarmDate.getTime();
        return null;
    }

    void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    long getCreatedDateInMillis() {
        return createdDate.getTime();
    }

    void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }
    public void addListItem(ListItem li) {
        listItems.add(li);
    }
    List<ListItem> getAllListItems() {
        return listItems;
    }
}
