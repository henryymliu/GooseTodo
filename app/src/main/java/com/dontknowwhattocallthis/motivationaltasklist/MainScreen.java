package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreen extends AppCompatActivity {
    ArrayList<HashMap<String,String>> taskData;
    SimpleAdapter adapter;
    Context ctx = this;
    private String taskName = "";
    private String taskDate = "sdff";
    public static final String dateFormat = "yyyy.MM.dd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setTitle("Tasks");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                taskAdder tA = new taskAdder(ctx,taskData,adapter);
                tA.addNewTask();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
        //add listeners


        //create test data //TODO: change dataset
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
        //TODO: Change adapter
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

    /*
    public void updateData(){
        HashMap<String,String> newTask = new HashMap<String, String>(2);
        newTask.put("task",taskName); newTask.put("date",taskDate);
        taskData.add(newTask);
        adapter.notifyDataSetChanged();
    }
    */
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
