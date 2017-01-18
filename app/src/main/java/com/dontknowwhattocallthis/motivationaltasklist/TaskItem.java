package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBHelper;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBSchema;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Henry Liu on 1/2/2017.
 */

public class TaskItem {
    private String title;
    //private Calendar taskDueDate;
    private Date duedate = Calendar.getInstance().getTime();
    private boolean usedate;
    private boolean usetime;
    private long id = -1;

    public TaskItem(){}

    public TaskItem(String name, Long millis, boolean ud, boolean ut){
        this.title = name;
        this.duedate = new Date(millis);
        this.usedate = ud;
        this.usetime = ut;
    }
    public TaskItem(Cursor cursor){
        this.id = cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable._ID));
        this.title = cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TITLE));
        this.duedate = new Date(cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP)));
        // Because Cursor doesn't support getBoolean for some reason
        this.usedate = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE))==1;
        this.usetime = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME))==1;
    }

    public String getTitle(){
        return this.title;
    }

    public Date getDueDate(){
        return this.duedate;
    }
    public boolean  hasDate(){return this.usedate;}
    public boolean hasTime(){return this.usetime;}

    public void setName(String newTitle){
        this.title = newTitle;
    }
    public void setDate(Date newDate){
        this.duedate = newDate;
    }
    public void setUseDate(boolean d){
        this.usedate = d;
    }
    public void setUseTime(boolean t){
        this.usetime =t;
    }

    public long getID() {
        return id;
    }
    public void setID(long ID) {
        this.id = ID;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_TITLE, this.title);
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP, this.duedate.getTime());
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME, this.usetime);
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE, this.usedate);

        return values;
    }

    public void writeToDataBase(TaskDBHelper mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(id < 0){
            // new entry, put and update id
            this.id = db.insert(TaskDBSchema.TaskTable.TABLE_NAME, null, this.getContentValues());
        }
        else{
            String selection = TaskDBSchema.TaskTable._ID + " = ?";
            String[] selectionArgs = { String.valueOf(this.id) };
            // updating entry
            db.update(
                    TaskDBSchema.TaskTable.TABLE_NAME,
                    this.getContentValues(),
                    selection,
                    selectionArgs);
        }
    }
}
