package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoemsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class PoemsFragment extends Fragment implements Data{
    //Конструктор должен быть пустым


    private SqlWorker sqlWorker;
    private View frameView;
    private RecyclerView recyclerView;
    private PoemsRecyclerView myPoemsRecyclerView;
    private Context context;
    private Bundle bundle;
    private String authorName;
    private boolean shouldUpdate;
    private Values values;
    public PoemsFragment() {
        //    super();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poets, null, false);
        recyclerView = (RecyclerView)frameView.findViewById(R.id.recyler_list_poets);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL,false));
        bundle = getArguments();
        setValues();
        setViewpagerTitle();
        myPoemsRecyclerView = new PoemsRecyclerView(context, getAuthorName(),values, this);
        recyclerView.setAdapter(myPoemsRecyclerView);
    }
    private void setViewpagerTitle() {
        TextView viewTitle = (TextView)getActivity().findViewById(R.id.toolbar_title);
        viewTitle.setText(getAuthorName());
        //setTypeFace(viewTitle);
    }
   /* private void setTypeFace(TextView textView) {
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/
    private String getAuthorName(){
        return bundle.getString(KEY_AUTHOR);
    }

    private Values getValues() {
        return sqlWorker.getPoemsTitlesFromDB(getAuthorName());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return frameView;
    }
    private void setValues(){
        values = getValues();
    }
    public void updateValues(int position){
        Values values = getValues();
        this.values.setStrings(values.getStrings());
        this.values.setStarrs(values.getStarrs());
        this.values.setIds(values.getIds());
        if(position != -1)
            myPoemsRecyclerView.notifyDataSetChanged();
    }
}
