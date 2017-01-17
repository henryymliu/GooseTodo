package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dontknowwhattocallthis.motivationaltasklist.model.TaskItemCursorAdapter;
import com.woxthebox.draglistview.DragItem;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreen extends AppCompatActivity {
    ArrayList<TaskItem> taskData;
    //TaskItemCursorAdapter adapter;
    Context ctx = this;
    private DragListView mDragListView;
    private MySwipeRefreshLayout mRefreshLayout;

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

                //taskAdder tA = new taskAdder(ctx,taskData,adapter);
                //tA.addNewTask();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
        //add listeners
        
        //create test data //TODO: change dataset
        String[] testData = {"Feed tiger", "Study", "Buy shrubberies"};
        Long[] testDataDates = {1484087306912L,1484087306912L,1484089306912L};
        Boolean[] testDataDateBool = {false, true, true};
        Boolean[] testDataTimeBool = {false, false, true};

        taskData = new ArrayList<TaskItem>();
        for(int i = 0;i < testData.length;i++){
            TaskItem temp = new TaskItem(testData[i],testDataDates[i],testDataDateBool[i],testDataTimeBool[i]);
            temp.setID(i);
            taskData.add(temp);
        }

        mRefreshLayout = (MySwipeRefreshLayout) this.findViewById(R.id.swipe_refresh_layout);

        mDragListView = (DragListView) this.findViewById(R.id.list_tasks);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);

        mRefreshLayout.setScrollingView(mDragListView.getRecyclerView());
        //mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.app_color));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        /*
        mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        mDragListView.setLayoutManager(new LinearLayoutManager(this));
        //SimpleAdapter adapter = new SimpleAdapter(this,taskData,android.R.layout.simple_list_item_2,new String[]{"task","date"}, new int[]{android.R.id.text1,android.R.id.text2});
        TaskItemCursorAdapter adapter = new TaskItemCursorAdapter(taskData,R.layout.task_item, R.id.item_layout, true);
        mDragListView.setAdapter(adapter, true);
        mDragListView.setCanDragHorizontally(true);
        mDragListView.setCustomDragItem(null);
    }

    private static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.task_item_task_desc)).getText();
            ((TextView) dragView.findViewById(R.id.task_item_task_desc)).setText(text);
            dragView.setBackgroundColor(dragView.getResources().getColor(R.color.colorPrimary));
        }
    }

    public void onTaskChecked(View v){
        DragListView lv = (DragListView) this.findViewById(R.id.list_tasks);
        if(((CheckBox) v).isChecked()){
            /*
            // TODO: Appropriately update task list
            int pos = lv.getPositionForView(v);
            Toast.makeText(MainScreen.this, "Task checked!" + pos, Toast.LENGTH_SHORT).show();
            taskData.remove(pos);
            adapter.notifyDataSetChanged();
            // warning: hack that functionally works but looks terrible
            ((CheckBox) v).setChecked(false);
            //lv.invalidateViews();
            */
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
