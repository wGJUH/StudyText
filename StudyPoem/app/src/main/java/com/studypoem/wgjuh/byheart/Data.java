package com.studypoem.wgjuh.byheart;

/**
 * Created by wGJUH on 10/2/2016.
 */

public interface Data {
    public static final String DB_NAME = "PoemsTable";
    public static final String DB_LOCATION = "/data/data/"+BuildConfig.APPLICATION_ID+"/databases/";
    /**
     * LIBS
     */
    public static final String LIBRARY = "Library";
    public static final String FAVORITES = "Favorites";
    /**
     * KEYS
     */
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_REGEX = "regex";
    public static final String KEY_NEW_TEXT = "new_text";
    public static final String KEY_LAST_POSITION = "last_position";
    public static final String KEY_AUTHOR = "author";

    /**
     * TODO проверить и по возмодности убрать лишние ключи
     */
    public static final String KEY_FROM_LIBRARY = "library";
    /**
     * LVL
     */
        public static final int LVL_POETS = 0;
        public static final int LVL_POEMS = 1;
    /**
     * Patterns
     */
    public static final String PATTERN_WORD = "[A-zA-Z0-9а-яА-ЯёЁ]+";
    public static final String PATTERN_STRING = "\\n\\r|\\r\\n|\\r|\\n|\\Z";
    /**
     * Columns
     */
    public static final String COLUMN_AUTHOR_NAME = "author_name";
    public static final String COLUMN_POEM_TITLE = "title";
    public static final String COLUMN_POEM= "poem";
    public static final String COLUMN_FAVORITE= "favorite";
    public static final String COLUMN_ID= "id_author";
    public static final String COLUMN_PORTRAIT_ID= "author_portrait_id";
    /**
     * NewTextStates
     */
    public static final int NEW_TEXT = 0;
    public static final int SAVE_TEXT = 1;
    /**
     * Request codes
     */
    public static final int LOAD_IMAGE = 1;

    /**
     * Result Codes
     */
   // public static final int LOAD_IMAGE = 1;

}
