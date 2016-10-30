package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoemsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class FavoriteFragment extends Fragment{

    private SqlWorker sqlWorker;
    private View frameView;
    private RecyclerView recyclerView;
    private PoemsRecyclerView myPoemsRecyclerView;
    private Context context;
    private Values values;
    public FavoriteFragment() {
        //super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poems, null, false);
        recyclerView = (RecyclerView)frameView.findViewById(R.id.recyler_list_poems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL,false));
        setValues();
        myPoemsRecyclerView = new PoemsRecyclerView(context, null, values, this);
        recyclerView.setAdapter(myPoemsRecyclerView);
    }

    private void setValues() {
        values = sqlWorker.getStarred();
    }

    @Override
    public void onPause() {
        System.out.println("PAUSE");
        super.onPause();
    }
    public void updateValues(int position,boolean favorite){
        Values values = sqlWorker.getStarred();
        System.out.println(values.getStrings());
        this.values.setStrings(values.getStrings());
        this.values.setStarrs(values.getStarrs());
        this.values.setIds(values.getIds());
        System.out.println(values.getStrings());
        System.out.println("position: " + position + " size: " + myPoemsRecyclerView.getItemCount());
        if(position != -1 && favorite)
        myPoemsRecyclerView.notifyItemRemoved(position);
        else myPoemsRecyclerView.notifyDataSetChanged();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return frameView;
    }
}
