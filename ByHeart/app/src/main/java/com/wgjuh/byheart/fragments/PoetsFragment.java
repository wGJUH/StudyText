package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.AuthorsGrid;
import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.TabbedActivity;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoetsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.Inflater;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class PoetsFragment extends AbstractFragment {
    private View frameView;
    private RecyclerView recyclerView;
    private LayoutManager gridManager;
    private SqlWorker sqlWorker;
    private Context context;
    private PoetsRecyclerView myPoetsRecyclerView;
    private Values values;
    private Boolean multiSelection = false;
    private HashMap<Integer, Boolean> booleanHashMap;
    private SparseBooleanArray sparseBooleanArray;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private int counter = 0;

    //Конструктор должен быть пустым
    public PoetsFragment() {
        //    super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        sparseBooleanArray = new SparseBooleanArray(1);
        sqlWorker = new SqlWorker(context);
        frameView = LayoutInflater.from(context).inflate(R.layout.fragment_poets, null, multiSelection);
        recyclerView = (RecyclerView) frameView.findViewById(R.id.recyler_list_poets);
        recyclerView.setLayoutManager(getGridManager());
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(toolbar != null)
        toolbarTextView = (TextView) toolbar.findViewById(R.id.toolbar_title);
        //GridView gridView = (GridView)frameView.findViewById(R.id.recyler_list_poets);
        setValues();

/*        final Picasso picasso = getPicasso();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    System.out.println("tag resume");
                    picasso.resumeTag(context);
                } else {
                    System.out.println("tag pause");
                    picasso.pauseTag(context);
                }
            }
        });
        myPoetsRecyclerView = new PoetsRecyclerView(context, values, picasso);*/
        myPoetsRecyclerView = new PoetsRecyclerView(this, values, sparseBooleanArray, this.multiSelection);
        ///// TODO: 31.10.2016 test lines below
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        ///
        recyclerView.setAdapter(myPoetsRecyclerView);

        //gridView.setAdapter(myPoetsRecyclerView);
    }

    private Picasso getPicasso() {
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        return builder.build();
    }

    private DisplayMetrics getMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ON_RESUME");
        setViewpagerTitle();
    }

    private void setViewpagerTitle() {
        TextView viewTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        viewTitle.setText(getString(R.string.app_name));
        // setTypeFace(viewTitle);
    }

  /*  private void setTypeFace(TextView textView) {
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/

    private LayoutManager getGridManager() {
        System.out.println("Get grid Manager");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return gridManager = new AuthorsGrid(context, 172 * getMetrics().densityDpi / 160 * 3);
        } else return gridManager = new AuthorsGrid(context, 172 * getMetrics().densityDpi / 160);
    }

    public Values getValues() {
        return sqlWorker.getPoemsAuthorsFromDB();
    }

    private void setValues() {
        values = getValues();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return frameView;
    }

    public void updateValues(int position) {
        Values values = getValues();
        this.values.setStrings(values.getStrings());
        this.values.setPortraitIds(values.getPortraitIds());
         myPoetsRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void favoriteItems() {

    }

    @Override
    public void toggleSelectAll() {
        //sparseBooleanArray = new SparseBooleanArray(values.getIds().size());
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
        myPoetsRecyclerView.notifyDataSetChanged();
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
        System.out.println("sparseSize: " + sparseBooleanArray.size());
        updateToolbar(multiSelection);
        myPoetsRecyclerView.notifyDataSetChanged();
    }

    private void updateToolbarCounter() {
        toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
    }

    public void updateToolbar(boolean isMultiSelection) {
        toolbar.getMenu().clear();
        if (isMultiSelection) {
            toolbarTextView.setText(sparseBooleanArray.size() + " " + getString(R.string.counter));
            toolbarTextView.setGravity(Gravity.END);
            toolbar.inflateMenu(R.menu.multiselection_menu);
            ((TabbedActivity) getActivity()).updateFabFunction(this, true);

        } else {
            System.out.println("update to standard");
            toolbarTextView.setText(getString(R.string.app_name));
            toolbarTextView.setGravity(Gravity.START);
            toolbar.inflateMenu(R.menu.menu);
            ((TabbedActivity) getActivity()).updateFabFunction(this, false);
        }
    }

    public Boolean getMultiSelection() {
        return multiSelection;
    }

    public void updateSelection(int position) {
        if (!sparseBooleanArray.get(position))
            sparseBooleanArray.put(position, true);
        else sparseBooleanArray.delete(position);
        System.out.println("array:" + sparseBooleanArray.toString() + " size: " + sparseBooleanArray.size());
        myPoetsRecyclerView.notifyItemChanged(position);
        updateToolbarCounter();
        if (sparseBooleanArray.size() == 0)
            setMultiSelection(false, position);
    }

    @Override
    public void deleteItems() {
        int[] selected = getSelected();
        String[] names = new String[selected.length];
        for (int i = 0; i < selected.length; i++) {
            names[i] = values.getStrings().get(selected[i]);
        }
        setMultiSelection(false, 0);
        sqlWorker.deleteByAuthor(names);
        updateValues(0);
        ((TabbedActivity) getActivity()).updateFavorites();
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
