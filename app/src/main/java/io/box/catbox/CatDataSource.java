package io.box.catbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CatDataSource {
    private SQLiteDatabase database;
    private CatBoxDbHelper dbHelper;
    private String[] allColumns = {
        CatBoxDbHelper.COLUMN_ID,
        CatBoxDbHelper.COLUMN_NAME,
        CatBoxDbHelper.COLUMN_RACE,
        CatBoxDbHelper.COLUMN_BIRTHDAY,
        CatBoxDbHelper.COLUMN_PICTURE
    };

    public CatDataSource(Context context) {
        dbHelper = new CatBoxDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long createCat(String name, String race, String birthday, String picture) {
        ContentValues values = new ContentValues();
        values.put(CatBoxDbHelper.COLUMN_NAME, name);
        values.put(CatBoxDbHelper.COLUMN_RACE, race);
        values.put(CatBoxDbHelper.COLUMN_BIRTHDAY, birthday);
        values.put(CatBoxDbHelper.COLUMN_PICTURE, picture);

        long insertId = database.insert(CatBoxDbHelper.TABLE_CATS, null, values);

        /*Cursor cursor = database.query(CatBoxDbHelper.TABLE_CATS,
                allColumns, CatBoxDbHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Cat newCat = cursorToCat(cursor);
        cursor.close();*/

        return insertId;
    }

    public void deleteCat(Cat cat) {
        long id = cat.getId();
        database.delete(CatBoxDbHelper.TABLE_CATS, CatBoxDbHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Cat> getAllCats() {
        List<Cat> cats = new ArrayList<Cat>();

        Cursor cursor = database.query(CatBoxDbHelper.TABLE_CATS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Cat cat = cursorToCat(cursor);
            cats.add(cat);
            cursor.moveToNext();
        }

        cursor.close();
        return cats;
    }

    public Cat getById(long catId) {
        Cat cat = new Cat();

        Cursor cursor = database.query(CatBoxDbHelper.TABLE_CATS,
                allColumns, CatBoxDbHelper.COLUMN_ID + " = " + catId, null, null, null, "1");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cat = cursorToCat(cursor);
        }

        cursor.close();

        return cat;
    }

    public long getCatCount() {
        return DatabaseUtils.queryNumEntries(database, CatBoxDbHelper.TABLE_CATS);
    }

    private Cat cursorToCat(Cursor cursor) {
        Cat cat = new Cat();
        cat.setId(cursor.getLong(0));
        cat.setName(cursor.getString(1));
        cat.setRace(cursor.getString(2));
        cat.setBirthday(cursor.getString(3));
        cat.setPicture(cursor.getString(4));
        return cat;
    }
}
