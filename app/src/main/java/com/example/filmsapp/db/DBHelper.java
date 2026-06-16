package com.example.filmsapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "movies.db";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "CREATE TABLE favorites (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "imdb_id TEXT UNIQUE," +
                        "title TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS favorites");
        onCreate(db);
    }


    public void addFavorite(String imdbId, String title)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("imdb_id", imdbId);
        values.put("title", title);

        db.insertWithOnConflict(
                "favorites",
                null,
                values,
                SQLiteDatabase.CONFLICT_IGNORE
        );
    }


    public ArrayList<String> getFavorites()
    {
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT imdb_id, title FROM favorites",
                null
        );

        while(cursor.moveToNext())
        {
            String imdbId = cursor.getString(0);
            String title = cursor.getString(1);

            list.add(title + "\n" + imdbId);
        }

        cursor.close();

        return list;
    }
}