package com.example.unmute.shoppinglistapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateShoppingListActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    return ShoppingListSettings.newInstance(position + 1);
                default:
                    return ShoppingListView.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "List Settings";
                case 1:
                    return "List View";
            }
            return null;
        }
    }

    public static List<ListItem> shoppingListItems = new ArrayList<ListItem>();

    public static class ShoppingListSettings extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        AutoCompleteTextView nameText;
        EditText noteText;
        AutoCompleteTextView groupText;
        EditText titleText;

        public ShoppingListSettings() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ShoppingListSettings newInstance(int sectionNumber) {
            ShoppingListSettings fragment = new ShoppingListSettings();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_shopping_list_settings, container, false);

            shoppingListItems = new ArrayList<ListItem>();

            Button addItemButton = (Button)rootView.findViewById(R.id.listSettings_AddItemButton);
            Button addGroupButton = (Button)rootView.findViewById(R.id.listSettings_AddGroupButton);
            Button saveButton = (Button)rootView.findViewById(R.id.saveShoppingListButton);

            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addItemToList();
                }
            });

            addGroupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addGroupToList();
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveShoppingList();
                }
            });

            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            nameText = (AutoCompleteTextView)getView().findViewById(R.id.listSetting_ItemNameEditText);
            noteText = (EditText)getView().findViewById(R.id.listSettings_NotesEditText);
            groupText = (AutoCompleteTextView)getView().findViewById(R.id.listSettings_GroupNameEditText);
            titleText = (EditText)getView().findViewById(R.id.listSettings_TitleEditText);

            DatabaseHandler db = new DatabaseHandler(this.getContext());
            List<Item> itemList = db.getAllItems();
            List<String> itemNameList = new ArrayList<String>();
            for (Item item : itemList) {
                itemNameList.add(item.getName());
            }
            List<ItemGroup> groupList = db.getAllItemGroups();
            List<String> groupNameList = new ArrayList<String>();
            for (ItemGroup group : groupList) {
                groupNameList.add(group.getName());
            }

            ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, itemNameList);
            ArrayAdapter<String> groupAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, groupNameList);

            if (!itemList.isEmpty())
                nameText.setAdapter(nameAdapter);
            if (!groupList.isEmpty())
                groupText.setAdapter(groupAdapter);
        }

        public void addItemToList() {
            String itemName = nameText.getText().toString();
            if (!itemName.isEmpty())
            {
                ListItem li = new ListItem();
                li.setName(itemName);
                li.setGathered(false);
                li.setNote(noteText.getText().toString());

                shoppingListItems.add(li);

                DatabaseHandler db = new DatabaseHandler(this.getContext());
                Item i = db.getItemByName(itemName);
                if (i.getName() == null) {
                    db.addItem(new Item(itemName));
                }
                db.close();
                Toast.makeText(getContext(), R.string.add_item_success_toast, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), R.string.add_item_empty_toast, Toast.LENGTH_SHORT).show();
            }
            nameText.setText("");
            noteText.setText("");
        }

        public void addGroupToList() {
            String groupName = groupText.getText().toString();
            if (!groupName.isEmpty())
            {
                DatabaseHandler db = new DatabaseHandler(this.getContext());
                ItemGroup ig = db.getItemGroupByName(groupName);
                if (ig.getName() != null) {
                    List<Item> itemList = ig.getAllItems();
                    for (Item item : itemList) {
                        ListItem li = new ListItem();
                        li.setName(item.getName());
                        li.setGathered(false);
                        shoppingListItems.add(li);
                    }
                    Toast.makeText(getContext(), R.string.add_group_success_toast, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), R.string.add_group_failure_toast, Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
            else {
                Toast.makeText(getContext(), R.string.add_group_empty_toast, Toast.LENGTH_SHORT).show();
            }
            groupText.setText("");
        }

        public boolean saveShoppingList() {
            EditText titleText = (EditText)getActivity().findViewById(R.id.listSettings_TitleEditText);
            String title = titleText.getText().toString();
            List<ListItem> list = shoppingListItems;
            if (!title.isEmpty() && !list.isEmpty())
            {
                DatabaseHandler db = new DatabaseHandler(this.getContext());
                ShoppingList sl = new ShoppingList(title, false, null);

                try {
                    db.addShoppingList(sl);
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(), R.string.exc_something_went_wrong, Toast.LENGTH_SHORT).show();
                    db.close();
                    return false;
                }

                Toast.makeText(getContext(), R.string.save_list_success_toast, Toast.LENGTH_SHORT).show();
                db.close();
                return true;
            }
            Toast.makeText(getContext(), R.string.save_list_empty_toast, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static class ShoppingListView extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        ListView listView;

        public ShoppingListView() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ShoppingListView newInstance(int sectionNumber) {
            ShoppingListView fragment = new ShoppingListView();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_shopping_list_view, container, false);
            listView = (ListView)rootView.findViewById(R.id.listView_ItemsListView);
            return rootView;
        }

        /*@Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);

            // Make sure that we are currently visible
            if (this.isVisible()) {
                onResume();
                // If we are becoming invisible, then...
                if (!isVisibleToUser) {
                    onPause();
                }
            }
        }*/

        @Override
        public void onResume() {
            super.onResume();
            loadItems();
        }

        public void loadItems() {
            CreateShoppingListAdapter adapter = new CreateShoppingListAdapter(this.getContext(), (ArrayList)shoppingListItems);
            listView.setAdapter(adapter);
        }

        public class CreateShoppingListAdapter extends ArrayAdapter<ListItem> {
            public CreateShoppingListAdapter(Context context, ArrayList<ListItem> listItems) {
                super(context, 0, listItems);
            }

            @Override
            public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
                ListItem li = getItem(position);

                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_create_shopping_list, parent, false);
                }

                TextView titleText = (TextView)convertView.findViewById(R.id.itemNameTextView);
                TextView noteText = (TextView)convertView.findViewById(R.id.notesTextView);

                if (li != null) {
                    titleText.setText(li.getName());
                    noteText.setText(li.getNote());
                }
                return convertView;
            }
        }
    }
}
