package com.example.unmute.shoppinglistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2; //Skapar databasen på nytt vid ökning av värdet.
    private static final String DATABASE_NAME = "shoppingListDB";

    private static final String TABLE_SHOPPING_LIST = "shoppingList";
    private static final String KEY_SL_ID = "id";
    private static final String KEY_SL_TITLE = "title";
    private static final String KEY_SL_SET_ALARM = "setAlert";
    private static final String KEY_SL_ALARM_DATETIME = "alertDateTime";
    private static final String KEY_SL_CREATED_DATETIME = "dateCreated";

    private static final String TABLE_LIST_ITEM = "listItem";
    private static final String KEY_LI_ID = "id";
    private static final String KEY_LI_NAME = "name";
    private static final String KEY_LI_GATHERED = "gathered";
    private static final String KEY_LI_NOTE = "note";
    private static final String KEY_LI_SL_ID = "shoppingListId";

    private static final String TABLE_ITEM_GROUP = "itemGroup";
    private static final String KEY_IG_ID = "id";
    private static final String KEY_IG_NAME = "name";

    private static final String TABLE_ITEM = "item";
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_NAME = "name";

    private static final String TABLE_ITEM_GROUP_ITEM = "itemGroup_Item";
    private static final String KEY_IGI_ITEM_GROUP_ID = "itemGroupId";
    private static final String KEY_IGI_ITEM_ID = "itemId";


    DatabaseHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SHOPPING_LIST_TABLE = "CREATE TABLE " + TABLE_SHOPPING_LIST + "("
                + KEY_SL_ID + " INTEGER PRIMARY KEY," + KEY_SL_TITLE + " VARCHAR(50),"
                + KEY_SL_SET_ALARM + " BIT NOT NULL," + KEY_SL_ALARM_DATETIME + " LONG,"
                + KEY_SL_CREATED_DATETIME + " LONG NOT NULL" + ")";

        String CREATE_LIST_ITEM_TABLE = "CREATE TABLE " + TABLE_LIST_ITEM + "(" + KEY_LI_ID
                + " INTEGER PRIMARY KEY," + KEY_LI_NAME + " VARCHAR(50) NOT NULL,"
                + KEY_LI_GATHERED + " BIT NOT NULL," + KEY_LI_NOTE + " TEXT,"
                + KEY_LI_SL_ID + " INTEGER NOT NULL," + "CONSTRAINT fk_ShoppingList FOREIGN KEY ("
                + KEY_LI_SL_ID + ") REFERENCES " + TABLE_SHOPPING_LIST + "(" + KEY_SL_ID + "))";

        String CREATE_ITEM_GROUP_TABLE = "CREATE TABLE " + TABLE_ITEM_GROUP + "("
                + KEY_IG_ID + " INTEGER PRIMARY KEY," + KEY_IG_NAME
                + " VARCHAR(50) NOT NULL" + ")";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + KEY_ITEM_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_NAME
                + " VARCHAR(50) NOT NULL" + ")";

        String CREATE_ITEM_GROUP_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM_GROUP_ITEM + "("
                + KEY_IGI_ITEM_GROUP_ID + " INTEGER," + KEY_IGI_ITEM_ID
                + " INTEGER," + " PRIMARY KEY (" + KEY_IGI_ITEM_GROUP_ID + ", " + KEY_IGI_ITEM_ID
                + "), " + " CONSTRAINT fk_ItemGroup FOREIGN KEY ("
                + KEY_IGI_ITEM_GROUP_ID + ") REFERENCES " + TABLE_ITEM_GROUP + "("
                + KEY_IG_ID + ")," + " CONSTRAINT fk_Item FOREIGN KEY ("
                + KEY_IGI_ITEM_ID + ") REFERENCES " + TABLE_ITEM + "("
                + KEY_ITEM_ID + ")" + ")";

        db.execSQL(CREATE_SHOPPING_LIST_TABLE);
        db.execSQL(CREATE_LIST_ITEM_TABLE);
        db.execSQL(CREATE_ITEM_GROUP_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_ITEM_GROUP_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_GROUP_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);

        onCreate(db);
    }

    /* CRUD METHODS */

    //SHOPPING LIST
    int addShoppingList(ShoppingList sl) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SL_TITLE, sl.getTitle());
        values.put(KEY_SL_SET_ALARM, (sl.getSetAlarm()) ? 1 : 0);
        values.put(KEY_SL_ALARM_DATETIME, sl.getAlarmDateInMillis());
        values.put(KEY_SL_CREATED_DATETIME, sl.getCreatedDateInMillis());

        int addedId = (int)db.insert(TABLE_SHOPPING_LIST, null, values); //Returnerar autoincrement-id för raden.
        for (ListItem item : sl.getAllListItems()) {
            item.setShoppingListId(addedId);
            addListItem(item);
        }
        db.close();
        return addedId;
    }
    public ShoppingList getShoppingList(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ShoppingList sl = new ShoppingList();

        String itemsQuery = "SELECT li." + KEY_LI_ID + ", li." + KEY_LI_NAME + ", li."
                + KEY_LI_GATHERED + ", li." + KEY_LI_NOTE + " FROM "
                + TABLE_SHOPPING_LIST + " sl INNER JOIN " + TABLE_LIST_ITEM + " li ON sl."
                + KEY_SL_ID + " = li." + KEY_LI_SL_ID + " WHERE sl."
                + KEY_SL_ID + " = " + id;

        List<ListItem> list = new ArrayList<>();

        Cursor itemsCursor = db.rawQuery(itemsQuery, null);

        if (itemsCursor.moveToFirst())
        {
            do {
                ListItem li = new ListItem();
                li.setId(itemsCursor.getInt(0));
                li.setName(itemsCursor.getString(1));
                li.setGathered((itemsCursor.getInt(2)) == 1);
                li.setNote(itemsCursor.getString(3));
                list.add(li);
            } while (itemsCursor.moveToNext());
            itemsCursor.close();
        }

        Cursor cursor = db.query(TABLE_SHOPPING_LIST, new String[]
                { KEY_SL_ID, KEY_SL_TITLE, KEY_SL_SET_ALARM, KEY_SL_ALARM_DATETIME, KEY_SL_CREATED_DATETIME },
                KEY_SL_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            sl.setId(cursor.getInt(0));
            sl.setTitle(cursor.getString(1));
            sl.setSetAlarm(cursor.getInt(2) == 1);
            sl.setAlarmDate(new Timestamp(cursor.getLong(3)));
            sl.setCreatedDate(new Timestamp(cursor.getLong(4)));
            sl.setListItems(list);
            cursor.close();
        }

        db.close();
        return sl;
    }
    List<ShoppingList> getAllShoppingLists() { //WITHOUT LISTITEMS
        List<ShoppingList> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_SHOPPING_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ShoppingList sl = new ShoppingList();
                sl.setId(cursor.getInt(0));
                sl.setTitle(cursor.getString(1));
                sl.setSetAlarm(cursor.getInt(2) == 1);
                sl.setAlarmDate(new Timestamp(cursor.getLong(3)));
                sl.setCreatedDate(new Timestamp(cursor.getLong(4)));

                list.add(sl);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public int getShoppingListsCount() {
        String query = "SELECT * FROM " + TABLE_SHOPPING_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();
        db.close();
        return cursor.getCount();
    }
    public int updateShoppingList(ShoppingList sl) { //TODO
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SL_TITLE, sl.getTitle());
        values.put(KEY_SL_SET_ALARM, (sl.getSetAlarm()) ? 1 : 0);
        values.put(KEY_SL_ALARM_DATETIME, sl.getAlarmDateInMillis());
        values.put(KEY_SL_CREATED_DATETIME, sl.getCreatedDateInMillis());

        int result = db.update(TABLE_SHOPPING_LIST, values, KEY_SL_ID + " = ?", new String[] { String.valueOf(sl.getId()) });
        db.close();
        return result;
    }
    public void deleteShoppingList(ShoppingList sl) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPPING_LIST, KEY_SL_ID + " = ?",
                new String[] { String.valueOf(sl.getId()) });
        db.close();
    }

    //ITEM GROUP
    void addItemGroup(ItemGroup ig) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues igValues = new ContentValues();
        igValues.put(KEY_IG_NAME, ig.getName());
        long id = db.insert(TABLE_ITEM_GROUP, null, igValues);

        List<Item> itemList = ig.getAllItems();

        //Fetches any items in the ItemGroup and puts them in the many-to-many table.
        for (Item item : itemList) {
            ContentValues igiValues = new ContentValues();
            igiValues.put(KEY_IGI_ITEM_GROUP_ID, id);
            igiValues.put(KEY_IGI_ITEM_ID, item.getId());
            db.insert(TABLE_ITEM_GROUP_ITEM, null, igiValues);
        }
        db.close();
    }
    public ItemGroup getItemGroup(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ItemGroup ig = new ItemGroup();

        String itemsQuery = "SELECT i." + KEY_ITEM_ID + ", i." + KEY_ITEM_NAME + " FROM "
                + TABLE_ITEM_GROUP_ITEM + " igi INNER JOIN " + TABLE_ITEM + " i ON igi."
                + KEY_IGI_ITEM_ID + " = i." + KEY_ITEM_ID + " WHERE igi." + KEY_IGI_ITEM_GROUP_ID
                + " = " + id;

        List<Item> list = new ArrayList<>();

        Cursor itemsCursor = db.rawQuery(itemsQuery, null);

        if (itemsCursor.moveToFirst())
        {
            do {
                Item item = new Item();
                item.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_ITEM_ID)));
                item.setName(itemsCursor.getString(itemsCursor.getColumnIndex(KEY_ITEM_NAME)));
                list.add(item);
            } while (itemsCursor.moveToNext());
            itemsCursor.close();
        }

        Cursor cursor = db.query(TABLE_ITEM_GROUP, new String[]
                        { KEY_IG_ID, KEY_IG_NAME },
                KEY_IG_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ig.setId(Integer.parseInt(cursor.getString(0)));
            ig.setName(cursor.getString(1));
            ig.setItemList(list);
            cursor.close();
        }

        db.close();
        return ig;
    }
    ItemGroup getItemGroupByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        ItemGroup ig = new ItemGroup();

        String itemsQuery = "SELECT i." + KEY_ITEM_ID + ", i." + KEY_ITEM_NAME + " FROM "
                + TABLE_ITEM + " i INNER JOIN " + TABLE_ITEM_GROUP_ITEM + " igi ON i."
                + KEY_ITEM_ID + " = igi." + KEY_IGI_ITEM_ID + " INNER JOIN " + TABLE_ITEM_GROUP
                + " ig ON igi." + KEY_IGI_ITEM_GROUP_ID + " = ig." + KEY_IG_ID + " WHERE ig."
                + KEY_IG_NAME + " = '" + name + "'";

        List<Item> list = new ArrayList<>();

        Cursor itemsCursor = db.rawQuery(itemsQuery, null);

        if (itemsCursor.moveToFirst())
        {
            do {
                Item item = new Item();
                item.setId(itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_ITEM_ID)));
                item.setName(itemsCursor.getString(itemsCursor.getColumnIndex(KEY_ITEM_NAME)));
                list.add(item);
            } while (itemsCursor.moveToNext());
            itemsCursor.close();
        }

        Cursor cursor = db.query(TABLE_ITEM_GROUP, new String[]
                        { KEY_IG_ID, KEY_IG_NAME },
                KEY_IG_NAME + " = ?", new String[] { name }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            ig.setId(Integer.parseInt(cursor.getString(0)));
            ig.setName(cursor.getString(1));
            ig.setItemList(list);
            cursor.close();
        }

        db.close();
        return ig;
    }
    List<ItemGroup> getAllItemGroups() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<ItemGroup> list = new ArrayList<>();

        String itemsQuery = "SELECT i." + KEY_ITEM_ID + ", i." + KEY_ITEM_NAME + " FROM "
                + TABLE_ITEM_GROUP_ITEM + " igi INNER JOIN " + TABLE_ITEM + " i ON igi."
                + KEY_IGI_ITEM_ID + " = i." + KEY_ITEM_ID + " WHERE igi." + KEY_IGI_ITEM_GROUP_ID
                + " = ?";

        String query = "SELECT * FROM " + TABLE_ITEM_GROUP;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ItemGroup ig = new ItemGroup();
                ig.setId(Integer.parseInt(cursor.getString(0)));
                ig.setName(cursor.getString(1));

                Cursor itemsCursor = db.rawQuery(itemsQuery, new String[] { cursor.getString(0) });
                if (itemsCursor.moveToFirst()) {
                    do {
                        Item i = new Item();
                        i.setId(Integer.parseInt(itemsCursor.getString(0)));
                        i.setName(itemsCursor.getString(1));
                        ig.addItem(i);
                    } while (itemsCursor.moveToNext());
                }
                itemsCursor.close();

                list.add(ig);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public int getItemGroupsCount() {
        String query = "SELECT * FROM " + TABLE_ITEM_GROUP;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();
        db.close();
        return cursor.getCount();
    }
    //UPDATE
    public void deleteItemGroup(ItemGroup ig) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM_GROUP, KEY_IG_ID + " = ?",
                new String[] { String.valueOf(ig.getId()) });
        db.close();
    }

    //ITEM
    void addItem(Item i) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, i.getName());

        db.insert(TABLE_ITEM, null, values);
        db.close();
    }
    //GET ITEM
    Item getItemByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Item item = new Item();

        Cursor cursor = db.query(TABLE_ITEM, new String[]
                        { KEY_ITEM_ID, KEY_ITEM_NAME },
                KEY_ITEM_NAME + " = ?", new String[] { name }, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            item.setId(Integer.parseInt(cursor.getString(0)));
            item.setName(cursor.getString(1));
            cursor.close();
        }

        db.close();
        return item;
    }
    List<Item> getAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Item> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_ITEM;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Item i = new Item();
                i.setId(Integer.parseInt(cursor.getString(0)));
                i.setName(cursor.getString(1));

                list.add(i);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public int getItemsCount() {
        String query = "SELECT * FROM " + TABLE_ITEM;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();
        db.close();
        return cursor.getCount();
    }

    //LISTITEM
    public void addListItem(ListItem li) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LI_NAME, li.getName());
        values.put(KEY_LI_GATHERED, li.getGathered()? 1 : 0);
        values.put(KEY_LI_NOTE, li.getNote());
        values.put(KEY_LI_SL_ID, li.getShoppingListId());

        db.insert(TABLE_LIST_ITEM, null, values);
        db.close();
    }
    //GET
    List<ListItem> getListItems(int shoppingListId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ListItem> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_LIST_ITEM + " WHERE " + KEY_LI_SL_ID + " = " + shoppingListId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ListItem li = new ListItem();
                li.setId(cursor.getInt(0));
                li.setName(cursor.getString(1));
                li.setGathered(cursor.getInt(2) == 1);
                li.setNote(cursor.getString(3));
                li.setShoppingListId(cursor.getInt(4));
                list.add(li);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    //COUNT
    //UPDATE
    //DELETE
}