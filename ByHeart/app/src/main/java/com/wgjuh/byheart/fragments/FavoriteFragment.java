package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.TabbedActivity;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoemsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class FavoriteFragment extends AbstractFragment{

    private SqlWorker sqlWorker;
    private View frameView;
    private RecyclerView recyclerView;
    private PoemsRecyclerView myPoemsRecyclerView;
    private Context context;
    private Values values;
    private SparseBooleanArray sparseBooleanArray;
    private int counter;
    private Boolean multiSelection = false;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    public FavoriteFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sparseBooleanArray = new SparseBooleanArray(1);
        context = getContext();
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poems, null, false);
        recyclerView = (RecyclerView)frameView.findViewById(R.id.recyler_list_poems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL,false));
        setValues();
        myPoemsRecyclerView = new PoemsRecyclerView(context, null, sparseBooleanArray ,values, this);
        recyclerView.setAdapter(myPoemsRecyclerView);
        toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if(toolbar != null)
        toolbarTextView = (TextView)toolbar.findViewById(R.id.toolbar_title);
    }

    private void setValues() {
        values = sqlWorker.getStarred();
        values.setPoetNames(sqlWorker.getAuthorFavorites());
    }

    @Override
    public void onPause() {
        System.out.println("PAUSE");
        super.onPause();
    }
    //// TODO: 06.01.2017 это должно помочь в задаче с добавлением поля имени автора в избранном 
    public void updateValues(int position,boolean favorite){
        Values values = sqlWorker.getStarred();
        System.out.println(values.getStrings());
        this.values.setPoetNames(sqlWorker.getAuthorFavorites());
        this.values.setStrings(values.getStrings());
        this.values.setStarrs(values.getStarrs());
        this.values.setIds(values.getIds());
        System.out.println(values.getStrings());
        System.out.println("position: " + position + " size: " + myPoemsRecyclerView.getItemCount());
        if(position != -1 && favorite)
        myPoemsRecyclerView.notifyItemRemoved(position);
        else myPoemsRecyclerView.notifyDataSetChanged();
        multiSelection = false;
        sparseBooleanArray.clear();
        //setMultiSelection(false,0);
    }

    @Override
    public void favoriteItems() {
        List<Integer> ids = new ArrayList<>();
        ArrayList<Integer>id_val = values.getIds();
        for(int i =0; i < sparseBooleanArray.size();i++){
            ids.add(id_val.get(sparseBooleanArray.keyAt(i)));
        }
        System.out.println("list ids contains: " + ids);
        sqlWorker.setSomeStars(ids,true);
        updateValues(0,false);
        AbstractFragment tempFragment = (AbstractFragment) getFragmentManager().findFragmentById(R.id.frame_root);
        tempFragment.updateValues(0);
        setMultiSelection(false,0);

    }

    @Override
    public void toggleSelectAll() {
        int size = values.getStrings().size();
        if(size != sparseBooleanArray.size()){
            for (int i = 0; i < size; i++){
                sparseBooleanArray.put(i,true);
            }
            updateToolbarCounter();
        }else {
            for (int i = 0; i < size; i++) {
                sparseBooleanArray.delete(i);
                setMultiSelection(false, 0);
            }
            setMultiSelection(false,0);
            updateToolbar(false);
        }
        myPoemsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void setMultiSelection(boolean multiSelection, int position) {
        this.multiSelection = multiSelection;
        if(multiSelection) {
            updateSelection(position);
            System.out.println("array:" + sparseBooleanArray.toString() + " multiselection: " + this.multiSelection);
        }else{
            counter = 0;
            sparseBooleanArray.clear();
        }
        updateToolbar(multiSelection);
        myPoemsRecyclerView.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return frameView;
    }


    public void updateSelection(int position) {
        System.out.println("sparseBooleanArray: " + sparseBooleanArray.size());
        if(!sparseBooleanArray.get(position))
            sparseBooleanArray.put(position,true);
        else sparseBooleanArray.delete(position);
        System.out.println("array:" + sparseBooleanArray.toString() + " size: " + sparseBooleanArray.size());
        myPoemsRecyclerView.notifyItemChanged(position);
        updateToolbarCounter();
        if (sparseBooleanArray.size() == 0)
            setMultiSelection(false,position);
    }
    private void updateToolbarCounter(){
        toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
    }
    public void updateToolbar(boolean isMultiSelection){

        toolbar.getMenu().clear();
        if(isMultiSelection) {
            toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
            toolbarTextView.setGravity(Gravity.END);
            toolbar.inflateMenu(R.menu.multiselection_favorites_menu);
            ((TabbedActivity)getActivity()).updateFabFunction(this,true);
        }else {
            //toolbarTextView.setText(getString(R.string.app_name));
            setViewpagerTitle();
            toolbarTextView.setGravity(Gravity.START);
            toolbar.inflateMenu(R.menu.menu);
            ((TabbedActivity)getActivity()).updateFabFunction(this,false);
        }
    }
    private void setViewpagerTitle() {
        TextView viewTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        viewTitle.setText(context.getString(R.string.app_name));
    }
    public Boolean getMultiSelection() {
        return multiSelection;
    }

    @Override
    public void deleteItems() {

    }

    @Override
    public int[] getSelected() {
        return new int[0];
    }
}
