package com.studypoem.wgjuh.studypoem;

import android.content.Intent;
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
import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class ListPoems extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
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
            String[] poets = getResources().getStringArray(R.array.poets);
            String[] titles = getResources().getStringArray(R.array.titles);
            System.out.println("Poets: " + Arrays.toString(poets) + " titles: " + Arrays.toString(titles));
            myRecyclerViewAdapter = new MyRecyclerViewAdapter(poets, titles, progress, this, true);
            recyclerView.setAdapter(myRecyclerViewAdapter);
                ((Toolbar)findViewById(R.id.toolbar_list_poems)).setTitle("Default Library");
            fab.setVisibility(View.INVISIBLE);}
            else{
                ((Toolbar)findViewById(R.id.toolbar_list_poems)).setTitle("My Library");

            }
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("STRING_TEST01");
    }
}
