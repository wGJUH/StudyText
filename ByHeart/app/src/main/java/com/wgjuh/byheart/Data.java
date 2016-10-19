package com.wgjuh.byheart;

import com.wgjuh.byheart.myapplication.BuildConfig;

/**
 * Created by wGJUH on 19.10.2016.
 */

public interface Data {
    public static final String DB_NAME = "PoemsTable";
    public static final String DB_LOCATION = "/data/data/"+ BuildConfig.APPLICATION_ID+"/databases/";
    /**
     * Columns
     */
    public static final String COLUMN_AUTHOR_NAME = "author_name";
    public static final String COLUMN_POEM_TITLE = "title";
    public static final String COLUMN_POEM= "poem";
    public static final String COLUMN_FAVORITE= "favorite";
    public static final String COLUMN_ID= "id_author";
    public static final String COLUMN_PORTRAIT_ID= "author_portrait_id";
}
