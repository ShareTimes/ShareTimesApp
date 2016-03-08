package com.timesmunch.timesmunch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sbabba on 3/8/16.
 */
public class NewsWireDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "times_munch.db";
    private static final String TABLE_NEWSWIRE = "NEWS_WIRE";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ARTICLE_TITLE = "article_title";
    public static final String COLUMN_ARTICLE_BYLINE = "article_byline";
    public static final String COLUMN_ARTICLE_DATE = "article_date";
    public static final String COLUMN_URL = "article_url";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_SECTION = "section";

    public static final String[] ALL_COLUMNS = new String[]{COLUMN_ID,COLUMN_ARTICLE_TITLE,COLUMN_ARTICLE_BYLINE,
    COLUMN_ARTICLE_DATE,COLUMN_URL,COLUMN_IMAGE_URL,COLUMN_SECTION};


    public NewsWireDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NEWSWIRE_TABLE = "CREATE TABLE " +
                TABLE_NEWSWIRE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ARTICLE_TITLE + " TEXT,"
                + COLUMN_ARTICLE_BYLINE + " TEXT,"
                + COLUMN_ARTICLE_DATE + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_SECTION + " TEXT)";
        db.execSQL(CREATE_NEWSWIRE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWSWIRE);
        onCreate(db);
    }

    public Cursor getAllArticles(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWSWIRE,
                ALL_COLUMNS,
                null,
                null,
                null,
                null,
                null);

        return cursor;
    }

    public Cursor getArticlesById(String id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWSWIRE,
                ALL_COLUMNS,COLUMN_ID+" = ?",
                new String[]{id},
                null,
                null,
                null);

        return cursor;
    }


}


