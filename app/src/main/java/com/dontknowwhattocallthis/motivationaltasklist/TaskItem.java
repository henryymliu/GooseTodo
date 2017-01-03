package com.dontknowwhattocallthis.motivationaltasklist;

import java.util.Calendar;

/**
 * Created by Henry Liu on 1/2/2017.
 */

public class TaskItem {
    private String taskTitle;
    private Calendar taskDueDate;

    public TaskItem(){

    }

    public String getTitle(){
        return taskTitle;
    }

    public Calendar getDueDate(){
        return taskDueDate;
    }

}
