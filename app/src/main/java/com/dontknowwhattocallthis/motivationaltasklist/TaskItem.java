package com.dontknowwhattocallthis.motivationaltasklist;

import android.database.Cursor;

import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBSchema;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Henry Liu on 1/2/2017.
 */

public class TaskItem {
    private String title;
    //private Calendar taskDueDate;
    private Date duedate;
    private boolean usedate;
    private boolean usetime;
    private long id;

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
        this.usedate = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME))==1;
        this.usetime = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE))==1;
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
}
