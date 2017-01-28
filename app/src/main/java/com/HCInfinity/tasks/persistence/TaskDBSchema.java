package com.HCInfinity.tasks.persistence;

import android.provider.BaseColumns;

/**
 * Created by Cheng on 03/01/2017.
 */

public final class TaskDBSchema {

    private TaskDBSchema(){}

    public static class TaskTable implements BaseColumns{
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ORDER = "mOrder";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
        public static final String COLUMN_NAME_USE_DATE = "usedate";
        public static final String COLUMN_NAME_USE_TIME = "usetime";
    }

}
