package com.wgjuh.byheart.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wGJUH on 19.10.2016.
 */

public class PoetsRecyclerView extends RecyclerView.Adapter<PoetsRecyclerView.ViewHolder> {
    private ArrayList<String> photos;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;
    private ViewHolder viewHolder;

    public PoetsRecyclerView(Values values) {
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poet_single_element, parent, false);
        viewHolder = new ViewHolder(singleElement);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView authorName = (TextView) holder.mLayout.findViewById(R.id.poem_author);
        authorName.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public Toolbar toolbar;
        public TextView toolbar_title;

        public ViewHolder(View v) {
            super(v);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
           /* if (context instanceof ListPoems) {
                *//*System.out.println(MainActivity.TAG + " context is instance of ListPoems");
                intent = getIntentStudyPoem(v);
                ListPoems.lvl++;
                v.getContext().startActivity(intent);*//*
            } else {
                System.out.println(MainActivity.TAG + " context is instance of ListPoets");
                intent = new Intent(context, ListPoems.class);
                intent.putExtra(KEY_REGEX, ((TextView) v.findViewById(R.id.poem_author)).getText().toString());
                ListPoems.lvl++;
                v.getContext().startActivity(intent);
            }*/
        }

        @Override
        public boolean onLongClick(View v) {
            /**
             * todo удалить после тестирования
             */
           // v.setSelected(!v.isSelected());
            return true;
        }
    }
}
