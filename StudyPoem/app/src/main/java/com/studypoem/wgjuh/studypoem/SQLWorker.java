package com.studypoem.wgjuh.studypoem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class SQLWorker extends SQLiteOpenHelper {
    public static final String DEFAULT_POEMS_TABLE_NAME = "PoemsTable";
    public static final String MY_POEMS_TABLE_NAME = "PoemsTable";
    Context context;
    public SQLWorker(Context context) {
        super(context, "LearnTextDB", null, 1);
        this.context = context;
    }

cd
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println(MainActivity.TAG + " CREATE db");
        db.execSQL("create table "+ DEFAULT_POEMS_TABLE_NAME+" ("
                +   "id_author integer primary key,"
                +   "author_name text,"
                +   "title text,"
                +   "poem text"
                +   ");");
        String[] authors = context.getResources().getStringArray(R.array.poets);// new String[]{"Пушкин А.С","Пушкин А.С","Лермонтов М.Ю.","Пушкин А.С","Пушкин А.С","Лермонтов М.Ю."};
        String[] titles = context.getResources().getStringArray(R.array.titles);//new String[]{"Зимнее утро", "Евгений Онегин","Бородино", "Я помню чудное мгновенье","Я Вас любил","Парус"};
        String[] poems = context.getResources().getStringArray(R.array.poems);
        System.out.println(MainActivity.TAG + " poems: " + Arrays.toString(poems)+ " names: " + Arrays.toString(authors) + " Titles: " + Arrays.toString(titles));
        ContentValues contentValues = new ContentValues();
        for(int i = 0 ; i < authors.length; i++){
            contentValues.clear();
            contentValues.put("author_name",authors[i]);
            contentValues.put("title",titles[i]);
            contentValues.put("poem",poems[i]);
            System.out.println(MainActivity.TAG+ " put into: " +  db.insert(DEFAULT_POEMS_TABLE_NAME,null,contentValues));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}