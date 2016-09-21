package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private String[] authors;
    private String[] titles;
    private int[] progress;
    private Context context;
    static int last_position;
    MyRecyclerViewAdapter(String[] authors, String[] titles, int[] progress, Context context){
        this.authors = authors;
        this.titles = titles;
        this.progress = progress;
        this.context = context;
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout mLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            v.setOnClickListener(this);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {
            int itemPosition = ((RecyclerView)v.getParent()).indexOfChild(v);
            System.out.println("Clicked and Position isViewHolder "+itemPosition + " last position" + last_position);
            Intent intent = new Intent(v.getContext(),StudyPoem.class);
            if(last_position != itemPosition)
            intent.putExtra("newText",true);
            last_position = itemPosition;
            v.getContext().startActivity(intent);
        }
    }
}
