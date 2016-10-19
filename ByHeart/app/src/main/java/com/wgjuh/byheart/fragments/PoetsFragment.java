package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgjuh.byheart.AuthorsGrid;
import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoetsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.zip.Inflater;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class PoetsFragment extends Fragment {
    private View frameView;
    private RecyclerView recyclerView;
    private LayoutManager gridManager;
    private SqlWorker sqlWorker;
    private Context context;
    private PoetsRecyclerView myPoetsRecyclerView;
    //Конструктор должен быть пустым
    public PoetsFragment() {
    //    super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poets, null, false);
        recyclerView = (RecyclerView)frameView.findViewById(R.id.recyler_list_poets);
        recyclerView.setLayoutManager(getGridManager());
        myPoetsRecyclerView = new PoetsRecyclerView(getValues());
        recyclerView.setAdapter(myPoetsRecyclerView);
    }
    private DisplayMetrics getMetrics(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    private LayoutManager getGridManager(){
        System.out.println("Get grid Manager");
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            return gridManager = new AuthorsGrid(context, 172 * getMetrics().densityDpi / 160 * 3);
        }else return gridManager = new AuthorsGrid(context, 172 * getMetrics().densityDpi / 160);
    }
    public Values getValues() {
        return sqlWorker.getPoemsAuthorsFromDB();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return frameView;
    }



}