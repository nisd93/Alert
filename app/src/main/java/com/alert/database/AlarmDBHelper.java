package com.alert.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class AlarmDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "alert.db";

    public AlarmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( "create table " + AlarmTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AlarmTable.Columns.UUID + ", " +
                AlarmTable.Columns.TIME + ", " +
                AlarmTable.Columns.HOUR + ", " +
                AlarmTable.Columns.MINUTE + ", " +
                AlarmTable.Columns.TITLE +
                ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {

        Log.w(this.getClass().getSimpleName(),
                "Upgrading database from version " + oldVer + " to " + newVer);

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + AlarmTable.NAME);

        // Create new table
        onCreate(db);
    }
}
