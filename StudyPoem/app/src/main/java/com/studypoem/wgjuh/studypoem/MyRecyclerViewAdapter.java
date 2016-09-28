package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    private Map<Integer,Map<String,Integer>> map;
    private String[] titles;
    private int[] progress;
    private Context context;
    private static boolean isFromDefaultLib = true;
    static int last_position = -1;

    MyRecyclerViewAdapter(ArrayList<String> strings,/* String[] titles,*/ int[] progress, Context context, Boolean isFromDefaultLib) {
        this.strings = strings;
        //this.titles = titles;
        this.progress = progress;
        this.context = context;
        this.isFromDefaultLib = isFromDefaultLib;
    }
    MyRecyclerViewAdapter(Values values, int[] progress, Context context, Boolean isFromDefaultLib) {
        this.strings = values.getStrings();
        this.ids = values.getIds();
        this.progress = progress;
        this.context = context;
        this.isFromDefaultLib = isFromDefaultLib;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poems_single_element, parent, false);
        ViewHolder viewHolder = new ViewHolder((CardView) v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ListPoems.lvl >= 1)
            ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setVisibility(View.GONE);
        else
            ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageResource(ids.get(position));
            ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setText(strings.get(position));

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mLayout;

        public ViewHolder(CardView v) {
            super(v);
            v.setOnClickListener(this);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            System.out.println("LVL!!!!!: " + ListPoems.lvl);
            if (ListPoems.lvl >= 1) {
                intent = getIntentStudyPoem(v);
            } else {
                intent = new Intent(v.getContext(), ListPoems.class);
                intent.putExtra("regex", ((TextView) v.findViewById(R.id.poem_author)).getText().toString());
                if (isFromDefaultLib)
                    intent.putExtra("activity", "defaulLibrary");
                else
                    intent.putExtra("activity", "myLibrary");


            }
            ListPoems.lvl++;
            v.getContext().startActivity(intent);
        }
    }

    @NonNull
    private static Intent getIntentStudyPoem(View v) {
        int itemPosition = ((RecyclerView) v.getParent()).indexOfChild(v);
        String[] titles = v.getContext().getResources().getStringArray(R.array.titles);
        int positionInMass = 0;
        for (String s : titles) {
            if (s.equals(((TextView) v.findViewById(R.id.poem_author)).getText()))
                break;
            positionInMass++;
        }

        System.out.println("Clicked and Position isViewHolder " + itemPosition + " last position" + last_position);
        Intent intent = new Intent(v.getContext(), StudyPoem.class);
        if (last_position != itemPosition) {
            intent.putExtra("newText", positionInMass);
        }
        intent.putExtra("lastposition", last_position);
        intent.putExtra("regex", ((TextView) v.findViewById(R.id.poem_author)).getText().toString());
        intent.putExtra("defaultPoems", isFromDefaultLib);
        last_position = itemPosition;
        return intent;
    }
}
