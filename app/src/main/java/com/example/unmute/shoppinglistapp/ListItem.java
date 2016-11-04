package com.example.unmute.shoppinglistapp;

public class ListItem {
    private int id;
    private String name;
    private boolean gathered;
    private String note;
    private int shoppingListId;

    public ListItem() {

    }

    public ListItem(String name, boolean gathered, String note) {
        this.name = name;
        this.gathered = gathered;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setGathered(boolean gathered) {
        this.gathered = gathered;
    }
    public boolean getGathered() {
        return gathered;
    }

    public void setNote(String note) {
        this.note = note;
    }
    public String getNote() {
        return note;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
    public int getShoppingListId() {
        return shoppingListId;
    }
}
