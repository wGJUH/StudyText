package com.studypoem.wgjuh.byheart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by WGJUH on 20.09.2016.
 */


public class ListPoems extends ListPoets {
    public static final String ListTag = " ListPoemsTag ";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainActivity.TAG+" ListPoemsTag " + " ListPoems OnCreate");
        setContentView(R.layout.list_poems);
        init();
    }
    @Override
    public Values getValues() {
        System.out.println(MainActivity.TAG+" ListPoemsTag " + " BUNDLE: " + bundle.getString(KEY_REGEX, null));
        return sqlWorker.getPoemsTitlesFromDB(bundle.getString(KEY_REGEX, null));
    }
    @Override
    public void init(){
        initViews();
        sqlWorker = new SQLWorker(this);
        mLayoutManager = new LinearLayoutManager(this);
        bundle = getIntent().getExtras();
        values = getValues();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, this, true);
        runSetters();
    }
    @Override
    public void runSetters() {
        recyclerView.setLayoutManager(mLayoutManager);
        fab.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        toolbar.setTitle(bundle.getString(KEY_REGEX,null));
        setSupportActionBar(toolbar);
    }
    @Override
    public void onClick(View v) {
        lvl++;
        Intent intent = new Intent(this, NewText.class);
        intent.putExtra(KEY_REGEX, toolbar.getTitle());
        intent.putExtra(KEY_NEW_TEXT, true);
        startActivityForResult(intent, 777);
    }

}
