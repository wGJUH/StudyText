package com.wgjuh.byheart;

import com.wgjuh.byheart.myapplication.BuildConfig;

/**
 * Created by wGJUH on 19.10.2016.
 */

public interface Data {
    String DB_NAME = "PoemsTable";
    String DB_LOCATION = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    /**
     * Columns
     */
    String COLUMN_AUTHOR_NAME = "author_name";
    String COLUMN_POEM_TITLE = "title";
    String COLUMN_POEM = "poem";
    String COLUMN_FAVORITE = "favorite";
    String COLUMN_ID = "id_author";
    String COLUMN_PORTRAIT_ID = "author_portrait_id";
    /**
     * Keys
     */
    String KEY_AUTHOR = "author";
    String KEY_TITLE = "title";

    /**
     * request codes
     */
    Integer REQUEST_LOAD_IMAGE = 1;
    Integer REQUEST_ADD_NEW_AUTHOR = 2;
    Integer REQUEST_ADD_NEW_POEM = 3;
    Integer REQUEST_ADD_NEW_FAVORITE = 4;

    /**
     * result codes
     */
    Integer ERROR = -1;
}
