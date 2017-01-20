package com.dontknowwhattocallthis.motivationaltasklist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.dontknowwhattocallthis.motivationaltasklist.model.TaskItemCursorAdapter;
import com.dontknowwhattocallthis.motivationaltasklist.model.TaskItemSQL;
import com.dontknowwhattocallthis.motivationaltasklist.persistence.TaskDBHelper;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity {
    private ArrayList<TaskItem> taskData = new ArrayList<TaskItem>();
    TaskDBHelper tDBHelper = new TaskDBHelper(this);
    private TaskItemCursorAdapter adapter;
    Context ctx = this;
    private DragListView mDragListView;

    taskHandler tA;



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


                tA.addNewTask();
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            }
        });
        //add listeners




        //create test data
        /*
        String[] testData = {"Feed tiger", "Study", "Buy shrubberies"};
        Long[] testDataDates = {1484087306912L,1484087306912L,1484089306912L};
        Boolean[] testDataDateBool = {false, true, true};
        Boolean[] testDataTimeBool = {false, false, true};
        for(int i = 0;i < testData.length;i++){
            TaskItem temp = new TaskItem(testData[i],testDataDates[i],testDataDateBool[i],testDataTimeBool[i]);

            taskData.add(temp);
            temp.writeToDataBase(tDBHelper);
        }

        */
        Cursor mCursor = TaskItemSQL.getAllTaskItems(tDBHelper);
        adapter = new TaskItemCursorAdapter(mCursor, R.layout.task_item, R.id.item_layout, true);
        tA = new taskHandler(ctx,taskData, tDBHelper, adapter);
        //mRefreshLayout = (MySwipeRefreshLayout) this.findViewById(R.id.swipe_refresh_layout);

        mDragListView = (DragListView) this.findViewById(R.id.list_tasks);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);



        mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {
                Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {
                    Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
                    TaskItemSQL.moveOrderTaskItem(tDBHelper, (long)fromPosition, (long)toPosition);
                    Cursor c = TaskItemSQL.getAllTaskItems(tDBHelper);
                    adapter.setCursor(c);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(this));
        mDragListView.setAdapter(adapter, true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(null);

    }

    public void onTaskChecked(View v){
       /// DragListView lv = (DragListView) this.findViewById(R.id.list_tasks);
        if(((CheckBox) v).isChecked()){

            //potential hack
            //Get tag of parent layout
            Long pos = (Long) ((ViewGroup) v.getParent()).getTag();
            tA.updateRemoveData(pos);

            Snackbar.make(findViewById(R.id.content_main_screen), "Task completed!", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: Implement undo item field
                            tA.addUndoTask();
                            //tA.updateAddData();
                        }
                    }).show();

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
