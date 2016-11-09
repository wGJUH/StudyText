package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.SqlWorker;
import com.wgjuh.byheart.StudyPoem;
import com.wgjuh.byheart.TabbedActivity;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.fragments.AbstractFragment;
import com.wgjuh.byheart.fragments.FavoriteFragment;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wGJUH on 19.10.2016.
 */

public class PoemsRecyclerView extends RecyclerView.Adapter<PoemsRecyclerView.ViewHolder> implements Data {
    private ArrayList<String> titles;
    private ArrayList<Boolean> starrs;
    private ViewHolder viewHolder;
    private Context context;
    private String authorName;
    private ArrayList<Integer> ids;
    private AbstractFragment fragment;
    private SqlWorker sqlWorker;
    private FragmentManager fragmentManager;
    private SparseBooleanArray sparseBooleanArray;
    private PoemsFragment poemsFragment;
    private FavoriteFragment favoriteFragment;
    public PoemsRecyclerView(Context context, String authorName , SparseBooleanArray sparseBooleanArray, Values values, AbstractFragment fragment) {
        this.context = context;
        titles = values.getStrings();
        starrs = values.getStarrs();
        this.authorName = authorName;
        this.ids = values.getIds();
        this.fragment = fragment;
        sqlWorker = new SqlWorker(context);
        this.sparseBooleanArray = sparseBooleanArray;
        fragmentManager = fragment.getFragmentManager();
        if(fragment instanceof  PoemsFragment)
            poemsFragment = (PoemsFragment)fragment;
                    else favoriteFragment = (FavoriteFragment)fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poem_single_element, parent, false);
        viewHolder = new PoemsRecyclerView.ViewHolder(singleElement);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView title = (TextView)holder.mLayout.findViewById(R.id.poem_title);
        ImageButton imageButton = (ImageButton)holder.mLayout.findViewById(R.id.button_favorite);
        setImageButton(imageButton,position);
        title.setText(titles.get(position));
        setViewSelected(holder, position);

        //setTypeFace(title);
    }
  /*  private void setTypeFace(TextView textView){
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/
  private void setViewSelected(ViewHolder viewHolder, int position) {
      //System.out.println("Selected boolean array: " + sparseBooleanArray.toString() + " position: " + position);
      if (sparseBooleanArray.get(position)) {
          //System.out.println("Make Selected: " + sparseBooleanArray.get(position));
          viewHolder.mLayout.setBackgroundColor(context.getResources().getColor(R.color.selected));
      } else {
         // System.out.println("Make Selected: " + sparseBooleanArray.get(position));
          viewHolder.mLayout.setBackgroundColor(Color.WHITE);
      }
  }

    private void setSelection(int position) {
        if(poemsFragment != null)
        poemsFragment.updateSelection(position);
        else favoriteFragment.updateSelection(position);
    }
    public void setImageButton(ImageButton imageButton, int position){
        //System.out.println(" pre finish");
        if(starrs.get((position))){
            imageButton.setImageResource(R.drawable.favorite_active);
        }else imageButton.setImageResource(R.drawable.favorite_disabled);
        imageButton.setSelected(starrs.get(position));

    }
    @Override
    public int getItemCount() {
        return titles.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public Toolbar toolbar;

        public ViewHolder(View v) {
            super(v);

            v.findViewById(R.id.button_favorite).setOnClickListener(this);
            mLayout = v;
            mLayout.setOnClickListener(this);
            mLayout.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent;
            boolean favorite;
            if(poemsFragment != null)
                favorite= poemsFragment.getMultiSelection();
            else favorite= favoriteFragment.getMultiSelection();

            if (!favorite)
            switch (v.getId()){
                case R.id.button_favorite:
                    System.out.println("Button favorite ");
                    int adapterPosition = getAdapterPosition();
                    if(adapterPosition != -1) {
                        sqlWorker.setStar(ids.get(adapterPosition));
                        updateValues(adapterPosition);
                    }
                    break;
                case R.id.singlePoemElement:
                    System.out.println(" CLICK ");
                    intent = new Intent(context, StudyPoem.class);
                    intent.putExtra(KEY_ID,ids.get(getAdapterPosition()));
                    context.startActivity(intent);
                    break;
                default:
                    System.out.println("Button other");
                    break;
            }
            else setSelection(getAdapterPosition());
        }
        private void updateValues(int position){
            if(fragment instanceof  FavoriteFragment) {
                fragment.updateValues(position, true);
                AbstractFragment tempFragment = (AbstractFragment)fragmentManager.findFragmentById(R.id.frame_root);
                if( tempFragment instanceof PoemsFragment){
                    tempFragment.updateValues(position);
                }
            }
            else {
                ((TabbedActivity)fragment.getActivity()).updateFavorites();
                fragment.updateValues(position);
            }
        }
        @Override
        public boolean onLongClick(View v) {
            /**
             * todo удалить после тестирования
             */
            boolean favorite;
            if(poemsFragment != null)
                favorite= poemsFragment.getMultiSelection();
            else favorite= favoriteFragment.getMultiSelection();
            System.out.println("fragment: " + fragment + " favorite: " + favorite);
            if(!favorite)
                setMultiSelectionMode(true);
            else setMultiSelectionMode(false);
            return true;
        }
        private void setMultiSelectionMode(Boolean multiSelectionMode) {
            if(poemsFragment != null)
            poemsFragment.setMultiSelection(multiSelectionMode, getAdapterPosition());
            else favoriteFragment.setMultiSelection(multiSelectionMode, getAdapterPosition());
        }
    }
}
