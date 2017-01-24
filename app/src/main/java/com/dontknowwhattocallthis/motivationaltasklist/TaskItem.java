package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.dontknowwhattocallthis.motivationaltasklist.model.TaskItemSQL;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBHelper;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBSchema;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Henry Liu on 1/2/2017.
 */

public class TaskItem {
    public static Date currDate;

    private String title;
    private Date dueDate = Calendar.getInstance().getTime();
    private boolean useDate;
    private boolean useTime;
    private boolean isOverdue = false;

    public static final long TASK_ID_NOT_SET = -1;
    public static final long TASK_ORDER_NOT_SET = -1;
    private long id = TASK_ID_NOT_SET;
    private long order = TASK_ORDER_NOT_SET;



    public TaskItem(){}

    public TaskItem(String name, Long millis, boolean ud, boolean ut){
        this.title = name;
        this.dueDate = new Date(millis);
        this.useDate = ud;
        this.useTime = ut;
    }
    public TaskItem(Cursor cursor){
        this.id = cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable._ID));
        this.order = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_ORDER));
        this.title = cursor.getString(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TITLE));
        this.dueDate = new Date(cursor.getLong(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP)));
        // Because Cursor doesn't support getBoolean for some reason

        this.useDate = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE))==1;
        this.useTime = cursor.getInt(cursor.getColumnIndex(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME))==1;
        TaskItem.currDate = Calendar.getInstance().getTime();

        checkIfOverdue();

    }
    //bunch o' getters n' setters
    public String getTitle(){
        return this.title;
    }
    public long getOrder(){
        return this.order;
    }
    public void setOrder(long o){this.order = o;};
    public Date getDueDate(){
        return this.dueDate;
    }
    public boolean  hasDate(){return this.useDate;}
    public boolean hasTime(){return this.useTime;}
    public boolean isOverdue(){return this.isOverdue;}
    public void setTitle(String newTitle){
        this.title = newTitle;
    }
    public void setDate(Date newDate){
        this.dueDate = newDate;
    }
    public void setUseDate(boolean d){
        this.useDate = d;
    }
    public void setUseTime(boolean t){
        this.useTime =t;
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
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_TIMESTAMP, this.dueDate.getTime());
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_TIME, this.useTime);
        values.put(TaskDBSchema.TaskTable.COLUMN_NAME_USE_DATE, this.useDate);

        return values;
    }

    public void writeToDataBase(TaskDBHelper mDbHelper){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if(id < 0){
            // if we haven't specified order, need to auto generate
            if(this.order == this.TASK_ORDER_NOT_SET) {
                long count = DatabaseUtils.queryNumEntries(db, TaskDBSchema.TaskTable.TABLE_NAME);
                this.order = count;
            }
            // new entry, put and update id
            this.id = TaskItemSQL.insertTaskItem(mDbHelper,this);
            //this.id = db.insert(TaskDBSchema.TaskTable.TABLE_NAME, null, this.getContentValues());
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

    //Removes ID from TaskItem to represent that it is no longer in the database
    public void invalidateIDFromDataBase(){

        this.id = this.TASK_ID_NOT_SET;
    }
    public void checkIfOverdue(){
        currDate = Calendar.getInstance().getTime();
        if(useDate) {
            assert(dueDate != null);
            //check if task overdue
            if(this.currDate.compareTo(this.dueDate) > 0){
                this.isOverdue = true;
            }
        }
    }
}
