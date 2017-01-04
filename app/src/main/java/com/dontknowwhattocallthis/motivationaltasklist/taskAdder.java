package com.dontknowwhattocallthis.motivationaltasklist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Henry on 1/4/2017.
 */

public class taskAdder {
    private final TaskItem task = new TaskItem();
    private Context ctx;
    private ArrayList<HashMap<String,String>> taskData;
    private SimpleAdapter adapter;

    public taskAdder(Context ctx, ArrayList<HashMap<String,String>> taskData, SimpleAdapter adapter){
        this.ctx = ctx;
        this.taskData = taskData;
        this.adapter = adapter;
    }
    public void addNewTask(){

        // final String taskName;
        //String taskDate = "jsdflkj"

        AlertDialog.Builder getTaskTitleBuilder = new AlertDialog.Builder(ctx);
        getTaskTitleBuilder.setTitle("Create new task");

        //create text field
        final EditText titleInput = new EditText(ctx);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setHint("Feed the cat");
        //set view in dialog box
        getTaskTitleBuilder.setView(titleInput);
        getTaskTitleBuilder.setPositiveButton("Select date", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                String taskName = titleInput.getText().toString();
                task.setName(taskName);
                setTaskDate();
                dialog.dismiss();
            }
        });
        getTaskTitleBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        getTaskTitleBuilder.setNeutralButton("Create", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                String taskName = titleInput.getText().toString();
                task.setName(taskName);
                task.setUseDate(false);task.setUseTime(false);
                updateData();
                dialog.dismiss();
            }
        });
        getTaskTitleBuilder.show();
    }

    //Launches datepicker dialog to select task date
    private void setTaskDate(){
        //initialize calendar to current date
        Calendar currCal = Calendar.getInstance();
        int cYear = currCal.get(Calendar.YEAR);
        int cMonth = currCal.get(Calendar.MONTH);
        int cDay = currCal.get(Calendar.DAY_OF_WEEK);

        final DatePickerDialog dpDialog = new DatePickerDialog(ctx,null,cYear,cMonth,cDay);
        dpDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Select time", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Collect date from picker
                DatePicker dp = dpDialog.getDatePicker();
                int uYear = dp.getYear();
                int uMonth = dp.getMonth(); //remember months go from 0-11
                int uDay = dp.getDayOfMonth();
               // Calendar cal = Calendar.getInstance();
                setTaskTime(new int[]{uYear,uMonth,uDay});
                task.setUseDate(true);
                dialog.dismiss();


            }
        });
        dpDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        dpDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Collect date from picker
                DatePicker dp = dpDialog.getDatePicker();
                int uYear = dp.getYear();
                int uMonth = dp.getMonth(); //remember months go from 0-11
                int uDay = dp.getDayOfMonth();
                Calendar cal = Calendar.getInstance();
                cal.set(uYear,uMonth,uDay,23,59,59); //set time to 23:59:59
                task.setDate(cal.getTime());
                task.setUseDate(true);
                task.setUseTime(false);
                updateData();
                dialog.dismiss();
            }
        });
        dpDialog.show();
    }

    private void setTaskTime(int[] dateParam){

    }
    private void updateData(){
        HashMap<String,String> newTask = new HashMap<String, String>(2);
        newTask.put("task",task.getTitle()); newTask.put("date",task.getDueDate().toString());
        taskData.add(newTask);
        adapter.notifyDataSetChanged();
    }
}
