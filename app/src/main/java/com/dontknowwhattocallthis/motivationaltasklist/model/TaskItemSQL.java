package com.dontknowwhattocallthis.motivationaltasklist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dontknowwhattocallthis.motivationaltasklist.TaskItem;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBHelper;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBSchema;

import static java.lang.Math.max;
import static java.lang.Math.min;


/**
 * Created by Cheng on 04/01/2017.
 */

public class TaskItemSQL {
    public enum OrderBy{
        ORDER,
        TIME
    }
    static String[] projection = {
            TaskDBSchema.TaskTable._ID,
            TaskDBSchema.TaskTable.COLUMN_NAME_ORDER,
            TaskDBSchema.TaskTable.COLUMN_NAME_TITLE,
            TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP,
            TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE,
            TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME
    };

    public static Cursor getAllTaskItems(TaskDBHelper dbhelper) {
        return getAllTaskItems(dbhelper, OrderBy.ORDER);
    }

    public static Cursor getAllTaskItems(TaskDBHelper dbhelper, OrderBy order){
        SQLiteDatabase db = dbhelper.getReadableDatabase();

        String selection = TaskDBSchema.TaskTable.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "My Title" };

        String sortOrder = null;
        switch(order) {
            case ORDER:
                sortOrder = TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " ASC";
                break;
            case TIME:
                sortOrder = TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP + " ASC";
                break;
        }

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

    public static int deleteAllTaskItems(TaskDBHelper dbHelper){
        int count  = dbHelper.getWritableDatabase().delete(TaskDBSchema.TaskTable.TABLE_NAME, null, null);
        return count;
    }

    //Given taskitem, inserts into database with given order
    //Prereq: task has already defined order
    //Params:
    //      mDbHelper - Helper used to access database
    //      task - TaskItem to insert to database
    //Returns: ID assigned by database
    public static Long insertTaskItem(TaskDBHelper mDbHelper, TaskItem task){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        assert(task.getOrder() != -1); //assert that order has already been assigned
        db.execSQL("UPDATE " + TaskDBSchema.TaskTable.TABLE_NAME + " SET "
                + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " = " +
                TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                " +1 WHERE " +
                TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " >= " +
                String.valueOf(task.getOrder()));
        return db.insert(TaskDBSchema.TaskTable.TABLE_NAME,null,task.getContentValues());
    }

    //Removes taskitem from database given ID from list.
    //Returns: TaskItem representing deleted task
    public static TaskItem deleteTaskItem(TaskDBHelper mDbHelper, long id){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = TaskDBSchema.TaskTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        //System.out.println(id);
        Cursor dC = mDbHelper.getReadableDatabase().query(
                TaskDBSchema.TaskTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        dC.moveToNext();
        TaskItem deletedItem = new TaskItem(dC);
        Long delOrder = deletedItem.getOrder();

        int count  = db.delete(TaskDBSchema.TaskTable.TABLE_NAME, selection, selectionArgs);
        // one and only one thing should have been deleted
        assert(count == 1);

        //update database order
        db = mDbHelper.getWritableDatabase();
        db.execSQL("UPDATE " + TaskDBSchema.TaskTable.TABLE_NAME + " SET "
                + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " = " +
                TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                " -1 WHERE " +
                TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " > " +
                String.valueOf(delOrder));

        return deletedItem;
    }

    //Updates ordering of tasks in database based on user-defined order.
    //Params: mDbHelper -Helper used to access database
    //          frompos - starting position of task in list before dragging
    //          topos - final position of task in list after dragging
    public static void moveOrderTaskItem(TaskDBHelper mDbHelper, long frompos, long topos){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(frompos < topos) {
            // First get the id for the frompos, so we can update it later
            long id = getTaskItemAtPos(mDbHelper, frompos).getID();
            // Next update all the pos from frompos+1 to topos
            /*
            String selection = TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " >? AND <=?";
            String[] selectionArgs = { String.valueOf(frompos), String.valueOf(topos) };
            ContentValues values = new ContentValues();
            values.put(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER, TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " - 1");
            db.update(
                    TaskDBSchema.TaskTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            */
            db.execSQL("UPDATE " + TaskDBSchema.TaskTable.TABLE_NAME + " SET "
                    + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " = " +
                    TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                     " -1 WHERE " +
                    TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + "> " +
                    String.valueOf(frompos) + " AND " + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                    "<= " + String.valueOf(topos));
            // update the mOrder of the original item to topos
            String selection = TaskDBSchema.TaskTable._ID + " = ?";
            String [] selectionID = { String.valueOf(id) };
            ContentValues values = new ContentValues();
            values.put(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER, topos);
            db.update(
                    TaskDBSchema.TaskTable.TABLE_NAME,
                    values,
                    selection,
                    selectionID);
        }
        if(frompos > topos) {
            // First get the id for the frompos, so we can update it later
            long id = getTaskItemAtPos(mDbHelper, frompos).getID();
            // Next update all the pos from frompos+1 to topos
            /*
            String selection = TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " >? AND <=?";
            String[] selectionArgs = { String.valueOf(frompos), String.valueOf(topos) };
            ContentValues values = new ContentValues();
            values.put(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER, TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " - 1");
            db.update(
                    TaskDBSchema.TaskTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            */
            db.execSQL("UPDATE " + TaskDBSchema.TaskTable.TABLE_NAME + " SET "
                    + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " = " +
                    TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                    " +1 WHERE " +
                    TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + ">= " +
                    String.valueOf(topos) + " AND " + TaskDBSchema.TaskTable.COLUMN_NAME_ORDER +
                    "< " + String.valueOf(frompos));
            // update the mOrder of the original item to topos
            String selection = TaskDBSchema.TaskTable._ID + " = ?";
            String [] selectionID = { String.valueOf(id) };
            ContentValues values = new ContentValues();
            values.put(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER, topos);
            db.update(
                    TaskDBSchema.TaskTable.TABLE_NAME,
                    values,
                    selection,
                    selectionID);
        }
    }

    //Returns TaskItem representing task at given position in list
    public static TaskItem getTaskItemAtPos(TaskDBHelper mDbHelper, long pos){
        String selection = TaskDBSchema.TaskTable.COLUMN_NAME_ORDER + " = ?";
        String[] selectionArgs = { String.valueOf(pos) };
        //System.out.println(id);
        Cursor dC = mDbHelper.getReadableDatabase().query(
                TaskDBSchema.TaskTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        dC.moveToNext();
        TaskItem item = new TaskItem(dC);
        dC.close();
        return item;
    }
}
