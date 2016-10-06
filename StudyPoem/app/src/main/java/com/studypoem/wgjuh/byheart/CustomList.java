package com.studypoem.wgjuh.byheart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by wGJUH on 06.10.2016.
 */

public abstract class CustomList extends AppCompatActivity implements View.OnClickListener, Data {
    public static int lvl =0 ;

    public  RecyclerView recyclerView;
    public  MyRecyclerViewAdapter myRecyclerViewAdapter;
    public  RecyclerView.LayoutManager mLayoutManager;
    public  FloatingActionButton fab;
    public  SQLWorker sqlWorker;
    public  Values values;
    public  Bundle bundle;
    public  Toolbar toolbar;

    public abstract void initViews();
    public abstract Values getValues();
    public abstract void runSetters();
    public abstract void init();
    public abstract void makeDialog();
}
