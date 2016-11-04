package com.example.unmute.shoppinglistapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shopping_list);

        ListView listView = (ListView)findViewById(R.id.listView);
        DatabaseHandler db = new DatabaseHandler(this);
        Bundle bundle = getIntent().getExtras();
        int shoppingListId = bundle.getInt("id");
        List<ListItem> listItems = db.getListItems(shoppingListId);

        ListItemAdapter adapter = new ListItemAdapter(this, (ArrayList)listItems);
        listView.setAdapter(adapter);
    }

    public class ListItemAdapter extends ArrayAdapter<ListItem> {
        public ListItemAdapter(Context context, ArrayList<ListItem> listItems) {
            super(context, 0, listItems);
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ListItem li = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_view_shopping_list, parent, false);
            }

            TextView nameText = (TextView)convertView.findViewById(R.id.nameText);
            TextView noteText = (TextView)convertView.findViewById(R.id.noteText);
            CheckBox gatheredCheckbox = (CheckBox)convertView.findViewById(R.id.gatheredCheckbox);

            nameText.setText(li.getName());
            noteText.setText(li.getNote());
            gatheredCheckbox.setChecked(li.getGathered());

            return convertView;
        }
    }
}
