package com.wgjuh.byheart;

import com.wgjuh.byheart.myapplication.BuildConfig;

/**
 * Created by wGJUH on 19.10.2016.
 */

public interface Data {
    String DB_NAME = "PoemsTable";
    String DB_UPGRADE_NAME = "PoemsTableUpgrade";
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
    String KEY_ID = "id";

    /**
     * request codes
     */
    Integer REQUEST_LOAD_IMAGE = 1;
    Integer REQUEST_ADD_NEW_AUTHOR = 2;
    Integer REQUEST_ADD_NEW_POEM = 3;
    Integer REQUEST_ADD_NEW_FAVORITE = 4;
    Integer PERMISSION_REQUEST_CODE = 5;

    /**
     * result codes
     */
    Integer ERROR = -1;
    /**
     * Pattern
     */
    public static final String PATTERN_WORD = "[A-zA-Z0-9а-яА-ЯёЁ]+";
    public static final String PATTERN_STRING = "\\n\\r|\\r\\n|\\r|\\n|\\Z|\\n\\n";
    /**
     * Color
     */
    public static final int DARK_GREEN = 0xff005948;
    public static final int DARK_GREEN_BRIGHT = 0x55005948;

}
