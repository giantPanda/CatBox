package io.box.catbox;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CatBoxDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_CATS = "cats";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_RACE = "race";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_PICTURE = "picture";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CatBoxDb";

    private static final String DATABASE_CREATE = "CREATE TABLE "
        + TABLE_CATS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_RACE + " TEXT NOT NULL, "
            + COLUMN_BIRTHDAY + " TEXT NOT NULL, "
            + COLUMN_PICTURE + " TEXT NOT NULL"
        + ");";

    public CatBoxDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATS);
        onCreate(db);
    }
}
