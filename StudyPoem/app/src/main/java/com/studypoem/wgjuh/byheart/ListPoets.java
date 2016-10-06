package com.studypoem.wgjuh.byheart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by WGJUH on 20.09.2016.
 */


public class ListPoets extends CustomList {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lvl--;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainActivity.TAG + " ListPoets OnCreate");
        setContentView(R.layout.list_poets);
        init();
    }
    @Override
    public void initViews(){
        toolbar = ((Toolbar) findViewById(R.id.toolbar_list_poems));
        recyclerView = (RecyclerView) findViewById(R.id.recyler_list_poems);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }
    private DisplayMetrics getMetrics(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
    @Override
    public Values getValues() {
        System.out.println(MainActivity.TAG + " ListPoets BUNDLE: " + bundle.getString(KEY_REGEX, null));
        return sqlWorker.getPoemsAuthorsFromDB();
    }

    @Override
    public void runSetters() {
        recyclerView.setLayoutManager(mLayoutManager);
        fab.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        toolbar.setTitle(bundle.getString(KEY_REGEX, null));
        setSupportActionBar(toolbar);
    }
    @Override
    public void init(){
        initViews();
        sqlWorker = new SQLWorker(this);
        mLayoutManager = new AuthorsGrid(this, 150 * getMetrics().densityDpi / 160);
        bundle = getIntent().getExtras();
        values = getValues();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, this, true);
        runSetters();
    }
    @Override
    public void makeDialog(){
        final View view = LayoutInflater.from(this).inflate(R.layout.new_author, null, false);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String author_name = ((EditText) view.findViewById(R.id.author_name)).getText().toString();
                if (!author_name.equals("")) {
                    sqlWorker.addStringToDB(author_name, null, null, false);
                    values.setStrings(getValues().getStrings());
                    myRecyclerViewAdapter.notifyItemInserted(sqlWorker.getRowNumber(author_name) - 1);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }

    @Override
    public void onClick(View v) {
           makeDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        values.setStrings(getValues().getStrings());
        myRecyclerViewAdapter.notifyDataSetChanged();
    }
}
