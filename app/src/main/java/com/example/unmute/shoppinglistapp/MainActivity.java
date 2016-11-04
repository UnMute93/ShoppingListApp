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

        Button createNewButton = (Button)this.findViewById(R.id.main_CreateNewButton);
        if (createNewButton != null)
            createNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(CreateShoppingListActivity.class);
                }
            });

        Button viewButton = (Button)this.findViewById(R.id.main_ViewListsButton);
        if (viewButton != null)
            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(ListShoppingListsActivity.class);
                }
            });

        Button addGroupButton = (Button)this.findViewById(R.id.main_AddItemGroupsButton);
        if (addGroupButton != null)
            addGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(AddItemGroupActivity.class);
                }
            });

        //Instick för manipulering av databas inom appen.
        Button dbDebugButton = (Button)this.findViewById(R.id.main_SettingsButton);
        if (dbDebugButton != null)
            dbDebugButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(AndroidDatabaseManager.class);
                }
            });
    }

    public void startActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
