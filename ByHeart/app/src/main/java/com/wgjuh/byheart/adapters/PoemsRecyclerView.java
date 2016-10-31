package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.wgjuh.byheart.fragments.FavoriteFragment;
import com.wgjuh.byheart.fragments.PoemsFragment;
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
    private Fragment fragment;
    private SqlWorker sqlWorker;
    private FragmentManager fragmentManager;
    public PoemsRecyclerView(Context context, String authorName , Values values, Fragment fragment) {
        this.context = context;
        titles = values.getStrings();
        starrs = values.getStarrs();
        this.authorName = authorName;
        this.ids = values.getIds();
        this.fragment = fragment;
        sqlWorker = new SqlWorker(context);
        fragmentManager = fragment.getFragmentManager();
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
        //setTypeFace(title);
    }
  /*  private void setTypeFace(TextView textView){
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/
    public void setImageButton(ImageButton imageButton, int position){
        System.out.println(" pre finish");
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
            v.setOnClickListener(this);
            v.findViewById(R.id.button_favorite).setOnClickListener(this);
            mLayout = v;
        }


        @Override
        public void onClick(View v) {
            Intent intent;
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
        }
        private void updateValues(int position){
            long start = System.currentTimeMillis();
            if(fragment instanceof FavoriteFragment){
                ((FavoriteFragment)fragment).updateValues(position,true);
                Fragment tempFragment = fragmentManager.findFragmentById(R.id.frame_root);
                if( tempFragment instanceof PoemsFragment){
                    ((PoemsFragment)tempFragment).updateValues(position);
                }
            }else if (fragment instanceof PoemsFragment){
                ((PoemsFragment)fragment).updateValues(position);
                /***
                 * todo убрать костыль с номером объекта из viewpager
                 */
                ((FavoriteFragment)(fragmentManager.findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 1))).updateValues(position,false);
            }
            System.out.println("finish: " + (System.currentTimeMillis() - start));
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
