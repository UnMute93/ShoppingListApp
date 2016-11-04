package com.example.unmute.shoppinglistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(myToolbar);

        Button createNewButton = (Button)this.findViewById(R.id.createNewButton);
        createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateShoppingListActivity(view);
            }
        });

        Button viewButton = (Button)this.findViewById(R.id.main_ViewListsButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListShoppingListsActivity();
            }
        });

        Button addGroupButton = (Button)this.findViewById(R.id.main_AddItemGroupsButton);
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddItemGroupActivity();
            }
        });

        //Instick för manipulering av databas inom appen.
        Button dbDebugButton = (Button)this.findViewById(R.id.settingsButton);
        dbDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDebug(view);
            }
        });
    }

    public void startCreateShoppingListActivity(View v) {
        Intent intent = new Intent(this, CreateShoppingListActivity.class);
        startActivity(intent);
    }

    public void startListShoppingListsActivity() {
        Intent intent = new Intent(this, ListShoppingListsActivity.class);
        startActivity(intent);
    }

    public void startAddItemGroupActivity() {
        Intent intent = new Intent(this, AddItemGroupActivity.class);
        startActivity(intent);
    }

    public void startDebug(View v) {
        Intent intent = new Intent(this, AndroidDatabaseManager.class);
        startActivity(intent);
    }
}
