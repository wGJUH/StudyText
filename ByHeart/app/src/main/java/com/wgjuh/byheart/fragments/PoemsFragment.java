package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.graphics.Typeface;
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

import com.wgjuh.byheart.Data;
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

public class PoemsFragment extends AbstractFragment implements Data {
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
    private SparseBooleanArray sparseBooleanArray;
    private int counter;
    private Boolean multiSelection = false;
    private Toolbar toolbar;
    private TextView toolbarTextView;

    public PoemsFragment() {
        //    super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sparseBooleanArray = new SparseBooleanArray(1);
        context = getContext();
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poets, null, false);
        recyclerView = (RecyclerView) frameView.findViewById(R.id.recyler_list_poets);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        bundle = getArguments();
        setValues();
        setViewpagerTitle();
        myPoemsRecyclerView = new PoemsRecyclerView(context, sparseBooleanArray, values, this);
        recyclerView.setAdapter(myPoemsRecyclerView);
    }

    public Boolean getMultiSelection() {
        return multiSelection;
    }

    private void setViewpagerTitle() {
        System.out.println("setViewpagerTitle: " +bundle.getString(KEY_AUTHOR));

        TextView viewTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        viewTitle.setText(getAuthorName());
    }
    private String getAuthorName() {
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

    private void setValues() {
        values = getValues();
    }

    public void updateValues(int position) {
        System.out.println("Update poems");
        Values values = getValues();
        this.values.setStrings(values.getStrings());
        this.values.setStarrs(values.getStarrs());
        this.values.setIds(values.getIds());
        if (position != -1)
            myPoemsRecyclerView.notifyDataSetChanged();
    }
    
    //// TODO: 02.11.2016 not working at that moment 
    @Override
    public void favoriteItems() {
        List<Integer> ids = new ArrayList<>();
        ArrayList<Integer>id_val = values.getIds();
        for(int i =0; i < sparseBooleanArray.size();i++){
            ids.add(id_val.get(sparseBooleanArray.keyAt(i)));
        }
        System.out.println("list ids contains: " + ids);
        sqlWorker.setSomeStars(ids,true);
        updateValues(0);
        ((TabbedActivity) getActivity()).updateFavorites();
        setMultiSelection(false, 0);

    }

    @Override
    public void toggleSelectAll() {
        int size = values.getStrings().size();
        if (size != sparseBooleanArray.size()) {
            for (int i = 0; i < size; i++) {
                sparseBooleanArray.put(i, true);
            }
            updateToolbarCounter();

        } else {
            for (int i = 0; i < size; i++) {
                sparseBooleanArray.delete(i);

            }
            setMultiSelection(false, 0);
            updateToolbar(false);

        }
        myPoemsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void setMultiSelection(boolean multiSelection, int position) {
        this.multiSelection = multiSelection;
        if (multiSelection) {
            updateSelection(position);
            System.out.println("array:" + sparseBooleanArray.toString() + " multiselection: " + this.multiSelection);
        } else {
            counter = 0;
            sparseBooleanArray.clear();
        }
        updateToolbar(multiSelection);
        myPoemsRecyclerView.notifyDataSetChanged();
    }

    public void updateSelection(int position) {
        if (!sparseBooleanArray.get(position))
            sparseBooleanArray.put(position, true);
        else sparseBooleanArray.delete(position);
        System.out.println("array:" + sparseBooleanArray.toString() + " size: " + sparseBooleanArray.size());
        myPoemsRecyclerView.notifyItemChanged(position);
        updateToolbarCounter();
        if (sparseBooleanArray.size() == 0)
            setMultiSelection(false, position);
    }

    private void updateToolbarCounter() {
        toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
    }

    public void updateToolbar(boolean isMultiSelection) {
        toolbar.getMenu().clear();
        if (isMultiSelection) {
            toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
            toolbarTextView.setGravity(Gravity.END);
            toolbar.inflateMenu(R.menu.multiselection_poems_menu);
            ((TabbedActivity) getActivity()).updateFabFunction(this, true);

        } else {
            setViewpagerTitle();
            toolbarTextView.setGravity(Gravity.START);
            toolbar.inflateMenu(R.menu.menu);
            ((TabbedActivity) getActivity()).updateFabFunction(this, false);
        }
    }

    @Override
    public void deleteItems() {
        ArrayList<Integer> ids = values.getIds();
        int[] selected = getSelected();
        String[] deletIds = new String[selected.length];
        for (int i = 0; i < selected.length; i++) {
            deletIds[i] = "" + ids.get(selected[i]);
        }
        setMultiSelection(false, 0);
        sqlWorker.deleteByIds(deletIds);
        updateValues(0);
        ((TabbedActivity) getActivity()).updateFavorites();

       /* for (int i =0;i< deletIds.length; i++){
            myPoemsRecyclerView.notifyItemRemoved(selected[i]);
            //myPoemsRecyclerView.notifyItemRangeChanged(selected[i], values.getIds().size());
        }*/
    }

    @Override
    public int[] getSelected() {
        int[] selected = new int[sparseBooleanArray.size()];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = sparseBooleanArray.keyAt(i);
        }
        return selected;
    }
}
