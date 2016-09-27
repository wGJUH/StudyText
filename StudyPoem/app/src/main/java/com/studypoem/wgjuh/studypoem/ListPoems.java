package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class ListPoems extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "SQL_test";
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    public static int lvl = 0;
    SQLWorker sqlWorker;
    Cursor cursor;
    String colum;
    String regex;
    ListPoems(){}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lvl--;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_poems);
        recyclerView = (RecyclerView)findViewById(R.id.recyler_list_poems);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Bundle bundle = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if(bundle!= null) {
            if(bundle.getString("activity").equals("defaulLibrary")){
            int[] progress = {0};
            myRecyclerViewAdapter = new MyRecyclerViewAdapter(getStringsFromDB(this,bundle.getString("regex",null)), progress, this, true);
            recyclerView.setAdapter(myRecyclerViewAdapter);
                ((Toolbar)findViewById(R.id.toolbar_list_poems)).setTitle("Default Library");
            fab.setVisibility(View.INVISIBLE);}
            else{
                ((Toolbar)findViewById(R.id.toolbar_list_poems)).setTitle("My Library");

            }
        }
    }
    public ArrayList<String> getStringsFromDB(Context context, String regex){
        sqlWorker = new SQLWorker(context);
        SQLiteDatabase sqLiteDatabase = sqlWorker.getReadableDatabase();
        if (lvl != 0 && regex != null) {
            switch (lvl) {
                case 0:
                    colum = "author_name";
                    break;
                case 1:
                    colum = "title";
                    break;
                case 2:
                    colum = "poem";
                    break;
                default:
                    break;
            }
            cursor = sqLiteDatabase.query(SQLWorker.DEFAULT_POEMS_TABLE_NAME, new String[]{colum}, "(author_name LIKE ? OR title LIKE ?)", new String[]{"%" + regex + "%", "%" + regex + "%"}, colum, null, colum);
        } else {

            colum = "author_name";
            cursor = sqLiteDatabase.query(SQLWorker.DEFAULT_POEMS_TABLE_NAME, new String[]{colum}, null, null, colum, null, colum);
        }
        String s = TAG;
        System.out.println(TAG + " " + cursor.getCount() + " names " + Arrays.toString(cursor.getColumnNames()));
        ArrayList<String> adapterStrings = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(cursor.getColumnCount() - 1);
                System.out.println("STRING: " + temp);
                adapterStrings.add(temp);
            } while (cursor.moveToNext());
        } else System.out.println(TAG + " 0 elements");
        System.out.println(TAG + adapterStrings.get(0).toString());
        sqLiteDatabase.close();
        return (adapterStrings);
    }

    @Override
    public void onClick(View v) {
        System.out.println("STRING_TEST01");
    }
}
