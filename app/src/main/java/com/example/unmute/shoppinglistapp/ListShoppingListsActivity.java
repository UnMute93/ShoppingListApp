package com.example.unmute.shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListShoppingListsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shopping_lists);

        DatabaseHandler db = new DatabaseHandler(this);
        List<ShoppingList> list = db.getAllShoppingLists();

        ListShoppingListsAdapter adapter = new ListShoppingListsAdapter(this, (ArrayList)list);
        ListView lv = (ListView)findViewById(R.id.shoppingListListView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(),ViewShoppingListActivity.class);
                ShoppingList sl = (ShoppingList)adapterView.getItemAtPosition(i);
                intent.putExtra("id", sl.getId());
                startActivity(intent);
            }
        });
    }

    public class ListShoppingListsAdapter extends ArrayAdapter<ShoppingList> {
        public ListShoppingListsAdapter(Context context, ArrayList<ShoppingList> shoppingLists) {
            super(context, 0, shoppingLists);
        }

        @Override
        public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
            ShoppingList sl = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_list_shopping_lists, parent, false);
            }

            TextView titleText = (TextView)convertView.findViewById(R.id.shoppingListTitleTextView);
            TextView dateText = (TextView)convertView.findViewById(R.id.shoppingListDateTextView);

            if (sl != null) {
                titleText.setText(sl.getTitle());
                SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy, HH:mm", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(sl.getCreatedDateInMillis());
                dateText.setText(formatter.format(calendar.getTime()));
            }
            return convertView;
        }
    }
}
