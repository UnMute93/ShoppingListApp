package com.example.unmute.shoppinglistapp;

public class Item {
    private int id;
    private String name;

    public Item() {

    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public Item(String name) {
        this.name = name;
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
}
