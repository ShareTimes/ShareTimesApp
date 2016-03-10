package com.timesmunch.timesmunch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sbabba on 3/8/16.
 */
public class NewsWireDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2

            ;
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

    private static final String ADD_DATA = "" +
            "INSERT INTO NEWS_WIRE VALUES" +
            "(NULL, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "?, " +
            "NULL)";


    public NewsWireDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context.getApplicationContext(), DATABASE_NAME, factory, DATABASE_VERSION);
    }


    private static NewsWireDBHelper instance;

    public static NewsWireDBHelper getInstance(Context context){
        if (instance == null){
            instance = new NewsWireDBHelper(context.getApplicationContext(),DATABASE_NAME, null,DATABASE_VERSION);
        }
        return instance;
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

    public Cursor getArticlesById(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWSWIRE,
                ALL_COLUMNS,COLUMN_ID+" = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        return cursor;
    }

    public String getArticleByline(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NEWSWIRE,
                new String[]{COLUMN_ARTICLE_BYLINE},
                COLUMN_ID + " LIKE ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(COLUMN_ARTICLE_BYLINE));
        } else {
            return "Not Found";
        }
    }


    public void insertBoth(ArrayList<StoryItem> results) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            SQLiteStatement sqLiteStatement = db.compileStatement(ADD_DATA);
            for (int i = 0; i < results.size(); i++) {
                sqLiteStatement.clearBindings();
                sqLiteStatement.bindString(1, results.get(i).getTitle());
                sqLiteStatement.bindString(2, results.get(i).getByline());
                sqLiteStatement.bindString(3, results.get(i).getPublished_date());
                sqLiteStatement.bindString(4, results.get(i).getUrl());
                sqLiteStatement.bindString(5, results.get(i).getPhotoUrl());

//                sqLiteStatement.bindString(5, results.get(i).getSection());

//                sqLiteStatement.bindString(4, results.get(i).getAbstract());

//                String CREATE_NEWSWIRE_TABLE = "CREATE TABLE " +
//                        TABLE_NEWSWIRE + "("
//                        + COLUMN_ID + " INTEGER PRIMARY KEY,"
//                        + COLUMN_ARTICLE_TITLE + " TEXT," 1
//                        + COLUMN_ARTICLE_BYLINE + " TEXT," 2
//                        + COLUMN_ARTICLE_DATE + " TEXT," 3
//                        + COLUMN_URL + " TEXT," 4
//                        + COLUMN_IMAGE_URL + " TEXT," 5
//                        + COLUMN_SECTION + " TEXT)"; 6



                Log.i("1", results.get(i).getTitle());
                Log.i("2", results.get(i).getPhotoUrl());
                Log.i("3", results.get(i).getByline());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("INSERTION ERROR", "Insertion Error: " + e.toString());
        } finally {
            db.endTransaction();
        }
    }


}


