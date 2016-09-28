package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class ListPoems extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "SQL_test";
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    public static int lvl = 0;
    SQLWorker sqlWorker;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lvl--;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_poems);
        sqlWorker = new SQLWorker(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyler_list_poems);
        if (lvl < 1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mLayoutManager = new AuthorsGrid(this, 150 * metrics.densityDpi / 160);
        } else mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        Bundle bundle = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (bundle != null) {
            if (bundle.getString("activity").equals("defaulLibrary")) {
                int[] progress = {0};
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(sqlWorker.getStringsFromDB(bundle.getString("regex", null)), progress, this, true);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                ((Toolbar) findViewById(R.id.toolbar_list_poems)).setTitle("Default Library");
                fab.setVisibility(View.INVISIBLE);
            } else {
                ((Toolbar) findViewById(R.id.toolbar_list_poems)).setTitle("My Library");

            }
        }
    }



    @Override
    public void onClick(View v) {
        System.out.println("STRING_TEST01");
    }
}
