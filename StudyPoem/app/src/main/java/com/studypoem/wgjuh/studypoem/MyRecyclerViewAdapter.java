package com.studypoem.wgjuh.studypoem;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private String[] authors;
    private String[] titles;
    private int[] progress;

    MyRecyclerViewAdapter(String[] authors, String[] titles, int[] progress){
        this.authors = authors;
        this.titles = titles;
        this.progress = progress;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poems_single_element, parent, false);
          ViewHolder viewHolder = new ViewHolder((LinearLayout) v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView)holder.mLayout.findViewById(R.id.poem_author)).setText(authors[position]);
        ((TextView)holder.mLayout.findViewById(R.id.poem_title)).setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return authors.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            mLayout = v;
        }
    }
}
