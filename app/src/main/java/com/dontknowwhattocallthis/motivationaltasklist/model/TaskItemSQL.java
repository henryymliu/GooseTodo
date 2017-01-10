package com.dontknowwhattocallthis.motivationaltasklist.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dontknowwhattocallthis.motivationaltasklist.TaskItem;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBHelper;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBSchema;

/**
 * Created by Cheng on 04/01/2017.
 */

public class TaskItemSQL {
    public static Cursor getAllTaskItems(TaskDBHelper dbhelper){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        
        String[] projection = {
                TaskDBSchema.TaskTable._ID,
                TaskDBSchema.TaskTable.COLUMN_NAME_TITLE,
                TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP,
                TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE,
                TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME
        };

        String selection = TaskDBSchema.TaskTable.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder =
                TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP + " ASC";

        Cursor cursor = db.query(
                TaskDBSchema.TaskTable.TABLE_NAME,        // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return cursor;
    }

    public static void putTaskItem(TaskItem task){

    }
}
