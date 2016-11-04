package com.example.unmute.shoppinglistapp;

import java.util.ArrayList;
import java.util.List;

class ItemGroup {
    private int id;
    private String name;
    private List<Item> itemList;

    ItemGroup() {
        this.itemList = new ArrayList<>();
    }

    public ItemGroup(int id, String name, List<Item> itemList) {
        this.id = id;
        this.name = name;
        this.itemList = itemList;
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

    void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
    void addItem(Item item) {
        itemList.add(item);
    }
    List<Item> getAllItems() {
        return itemList;
    }
    public Item getItem(int id) {
        Item result = new Item();
        for (Item item : itemList) {
            if (item.getId() == id)
                result = item;
        }
        return result;
    }
}
