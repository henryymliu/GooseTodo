package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
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
    private Date currDate;
    private boolean usedate;
    private boolean usetime;
    private static final long ID_NOT_SET = -1;
    private static final long ORDER_NOT_SET = -1;
    private long id = ID_NOT_SET;
    private long order = ORDER_NOT_SET;
    private boolean isOverdue = false;


    public TaskItem(){}

    public TaskItem(String name, Long millis, boolean ud, boolean ut){
        this.title = name;
        this.duedate = new Date(millis);
        this.usedate = ud;
        this.usetime = ut;
    }
    public TaskItem(Cursor cursor){
        this.id = cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable._ID));
        this.order = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER));
        this.title = cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TITLE));
        this.duedate = new Date(cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP)));
        // Because Cursor doesn't support getBoolean for some reason

        this.usedate = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE))==1;
        this.usetime = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME))==1;
        this.currDate = Calendar.getInstance().getTime();
        if(usedate) {
            assert(duedate != null);
            //check if task overdue
            if(this.currDate.compareTo(this.duedate) > 0){
                this.isOverdue = true;
            }
        }

    }
    //bunch o' getters n' setters
    public String getTitle(){
        return this.title;
    }
    public long getOrder(){
        return this.order;
    }
    public Date getDueDate(){
        return this.duedate;
    }
    public boolean  hasDate(){return this.usedate;}
    public boolean hasTime(){return this.usetime;}
    public boolean isOverdue(){return this.isOverdue;}
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
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER, this.order);
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP, this.duedate.getTime());
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME, this.usetime);
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE, this.usedate);

        return values;
    }

    public void writeToDataBase(TaskDBHelper mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(id < 0){
            // if we haven't specified order, need to auto generate
            if(this.order == this.ORDER_NOT_SET) {
                long count = DatabaseUtils.queryNumEntries(db, TaskDBSchema.TaskTable.TABLE_NAME);
                this.order = count;
            }
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

    public void deleteFromDataBase(TaskDBHelper mDbHelper){
        String selection = TaskDBSchema.TaskTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(this.id) };
        int count  = mDbHelper.getWritableDatabase().delete(TaskDBSchema.TaskTable.TABLE_NAME, selection, selectionArgs);
        // one and only one thing should have been deleted
        assert(count == 1);
        this.id = this.ID_NOT_SET;
    }
}
