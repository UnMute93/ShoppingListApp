package com.example.unmute.shoppinglistapp;

class ListItem {
    private int id;
    private String name;
    private boolean gathered;
    private String note;
    private int shoppingListId;

    ListItem() {

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

    void setGathered(boolean gathered) {
        this.gathered = gathered;
    }
    boolean getGathered() {
        return gathered;
    }

    void setNote(String note) {
        this.note = note;
    }
    String getNote() {
        return note;
    }

    void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
    int getShoppingListId() {
        return shoppingListId;
    }
}
