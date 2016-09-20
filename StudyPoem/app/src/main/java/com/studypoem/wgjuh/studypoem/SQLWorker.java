package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class SQLWorker extends SQLiteOpenHelper {
    public SQLWorker(Context context) {
        super(context, "LearnTextDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("CREATE db");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
