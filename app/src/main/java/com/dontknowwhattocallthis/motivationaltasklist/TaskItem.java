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

    public TaskItem(){}

    public TaskItem(Cursor cursor){
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
}
