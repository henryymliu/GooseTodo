package com.dontknowwhattocallthis.motivationaltasklist.model;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.dontknowwhattocallthis.motivationaltasklist.R;
import com.dontknowwhattocallthis.motivationaltasklist.TaskItem;

/**
 * Created by Cheng on 03/01/2017.
 */

public class TaskItemCursorAdapter extends ResourceCursorAdapter {

    public TaskItemCursorAdapter(Context context, int layout, Cursor cursor, int flags){
        super(context, layout, cursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Create a new TaskItem using the cursor
        TaskItem taskitem = new TaskItem(cursor);

        // Assign the relevant fields
        TextView title = (TextView) view.findViewById(R.id.task_item_task_desc);
        title.setText(taskitem.getTitle());

        TextView date = (TextView) view.findViewById(R.id.task_item_task_date);
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        date.setText(dateFormat.format(taskitem.getDueDate()));
    }
}
