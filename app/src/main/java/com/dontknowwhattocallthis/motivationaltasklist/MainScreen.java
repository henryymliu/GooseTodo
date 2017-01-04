package com.dontknowwhattocallthis.motivationaltasklist;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreen extends AppCompatActivity {
    ArrayList<HashMap<String,String>> taskData;
    SimpleAdapter adapter;
    private String taskName = "";
    private String taskDate = "sdff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add tasks with this action
                addNewTask();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
        //add listeners


        //create test data
        String[] testData = {"Feed tiger: Done", "Study", "Buy shrubberies"};
        String[] testDataDates = {"Today, 7:00 PM","","January 13"};
        taskData = new ArrayList<HashMap<String,String>>();
        for(int i = 0;i < 3;i++){
            HashMap<String,String> temp = new HashMap<String,String>(2);
            temp.put("task",testData[i]);
            temp.put("date",testDataDates[i]);
            taskData.add(temp);
        }
        ListView listView = (ListView) findViewById(R.id.list_tasks);
        //SimpleAdapter adapter = new SimpleAdapter(this,taskData,android.R.layout.simple_list_item_2,new String[]{"task","date"}, new int[]{android.R.id.text1,android.R.id.text2});
        adapter = new SimpleAdapter(this,taskData,R.layout.task_item,new String[]{"task","date"}, new int[]{R.id.task_item_task_desc,R.id.task_item_task_date});
        listView.setAdapter(adapter);
    }
    public void onTaskChecked(View v){
        ListView lv = (ListView) this.findViewById(R.id.list_tasks);
        if(((CheckBox) v).isChecked()){
            // TODO: Appropriately update task list
            int pos = lv.getPositionForView(v);
            Toast.makeText(MainScreen.this, "Task checked!" + pos, Toast.LENGTH_SHORT).show();
            taskData.remove(pos);
            adapter.notifyDataSetChanged();
            // warning: hack that functionally works but looks terrible
            ((CheckBox) v).setChecked(false);
            //lv.invalidateViews();

        }
    }
    public void addNewTask(){
        //TODO: Replace with TaskItem object
       // final String taskName;
        //String taskDate = "jsdflkj";

        AlertDialog.Builder getTaskTitleBuilder = new AlertDialog.Builder(this);
        getTaskTitleBuilder.setTitle("Create new task");

        //create text field
        final EditText titleInput = new EditText(this);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);

        //set view in dialog box
        getTaskTitleBuilder.setView(titleInput);
        getTaskTitleBuilder.setPositiveButton("Select date", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){
                taskName = titleInput.getText().toString();
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
                taskName = titleInput.getText().toString();
                updateData();
                dialog.dismiss();
            }
        });
        getTaskTitleBuilder.show();
    }

    //Launches datepicker dialog to select task date
    public void setTaskDate(){
        Calendar currCal = Calendar.getInstance();
        int cYear = currCal.get(Calendar.YEAR);
        int cMonth = currCal.get(Calendar.MONTH);
        int cDay = currCal.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog dpDialog = new DatePickerDialog(this,null,cYear,cMonth,cDay);
        dpDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Select time", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Collect date from picker

            }
        });
    }

    public void updateData(){
        HashMap<String,String> newTask = new HashMap<String, String>(2);
        newTask.put("task",taskName); newTask.put("date",taskDate);
        taskData.add(newTask);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
