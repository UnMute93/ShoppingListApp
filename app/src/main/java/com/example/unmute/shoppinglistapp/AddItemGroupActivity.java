package com.example.unmute.shoppinglistapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddItemGroupActivity extends AppCompatActivity {

    List<String> itemList = new ArrayList<String>();
    AutoCompleteTextView itemNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_group);

        Button addItemButton = (Button)findViewById(R.id.addItemButton);
        Button saveGroupButton = (Button)findViewById(R.id.group_SaveButton);
        itemNameTextView = (AutoCompleteTextView)findViewById(R.id.group_AddItemEditText);

        DatabaseHandler db = new DatabaseHandler(this);
        List<Item> items = db.getAllItems();
        List<String> itemNameList = new ArrayList<String>();
        for (Item item : items) {
            itemNameList.add(item.getName());
        }

        ArrayAdapter<String> itemNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemNameList);

        if (!itemNameList.isEmpty())
            itemNameTextView.setAdapter(itemNameAdapter);

        ListView listView = (ListView)findViewById(R.id.group_ItemListVIew);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemToList(adapter);
            }
        });

        saveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItemGroup(adapter);
            }
        });
    }

    public void addItemToList(ArrayAdapter<String> adapter) {
        String itemName = itemNameTextView.getText().toString();
        if (!itemName.isEmpty())
        {
            DatabaseHandler db = new DatabaseHandler(this);
            Item item = db.getItemByName(itemName);
            if (item.getName() == null) {
                db.addItem(new Item(itemName));
            }
            db.close();
            itemList.add(itemName);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, R.string.group_add_item_success_toast, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, R.string.group_add_item_empty_toast, Toast.LENGTH_SHORT).show();
        }
        itemNameTextView.setText("");
    }

    public void saveItemGroup(ArrayAdapter<String> adapter) {
        EditText groupNameTextView = (EditText)findViewById(R.id.group_NameOfGroupEditText);
        String groupName = groupNameTextView.getText().toString();
        if (!groupName.isEmpty() && !itemList.isEmpty()) {
            DatabaseHandler db = new DatabaseHandler(this);
            ItemGroup ig = db.getItemGroupByName(groupName);

            if (ig.getName() == null) {
                ig = new ItemGroup();
                ig.setName(groupName);
                List<Item> items = new ArrayList<Item>();
                for (String name : itemList) {
                    Item item = db.getItemByName(name);
                    items.add(item);
                }
                ig.setItemList(items);
                db.addItemGroup(ig);
                Toast.makeText(this, R.string.group_save_group_success_toast, Toast.LENGTH_SHORT).show();
            }
            db.close();
        }
        else {
            Toast.makeText(this, R.string.group_save_group_empty_toast, Toast.LENGTH_SHORT).show();
        }
        groupNameTextView.setText("");
    }
}
