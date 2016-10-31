package com.wgjuh.byheart.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.AuthorsGrid;
import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.adapters.PoetsRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.zip.Inflater;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;

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
    private Values values;

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
        recyclerView = (RecyclerView) frameView.findViewById(R.id.recyler_list_poets);
        recyclerView.setLayoutManager(getGridManager());
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
        myPoetsRecyclerView = new PoetsRecyclerView(context, values);
        ///// TODO: 31.10.2016 test lines below
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);
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
        // myPoetsRecyclerView.notifyItemInserted(position);
    }

}
