package com.dontknowwhattocallthis.motivationaltasklist.persistence;

/**
 * Created by Cheng on 03/01/2017.
 * TaskDBHelper raps SQLiteOpenHelper and should be used to make read and write
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Task.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskDBSchema.TaskTable.TABLE_NAME + " (" +
                    TaskDBSchema.TaskTable._ID + " INTEGER PRIMARY KEY," +
                    TaskDBSchema.TaskTable.COLUMN_NAME_TITLE + " TEXT," +
                    TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP + " BIGINT," +
                    TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE + " BIT," +
                    TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME + " BIT" + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskDBSchema.TaskTable.TABLE_NAME;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now delete everything. Migrations need to be implemented.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}