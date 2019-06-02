package kr.saintdev.bandhelp.core.database;
/**
 * Copyright (c) 2015-2019 Saint software All rights.
 * Team. 09:19
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GarupaDBHelper extends SQLiteOpenHelper {
    interface SQLQuery {
        String CREATE_TABLE_PLAYTIME = "CREATE TABLE garupa_play_time (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "play_time INTEGER NOT NULL," +
                "play_date TEXT NOT NULL," +
                "play_nation TEXT NOT NULL);";
    }

    public GarupaDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLQuery.CREATE_TABLE_PLAYTIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getReadOnly() {
        return this.getReadableDatabase();
    }

    public SQLiteDatabase getWritable() {
        return this.getWritableDatabase();
    }
}
