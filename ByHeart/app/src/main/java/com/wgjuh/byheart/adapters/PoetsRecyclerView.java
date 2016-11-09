package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.myapplication.BuildConfig;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wGJUH on 19.10.2016.
 */
public class PoetsRecyclerView extends RecyclerView.Adapter<PoetsRecyclerView.ViewHolder> implements Data {
    private ArrayList<String> photos;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;
    private ViewHolder viewHolder;
    private Fragment context;
    private Picasso picasso;
    private Boolean multiSelection;
    private SparseBooleanArray sparseBooleanArray;

    public PoetsRecyclerView(Fragment context, Values values, Picasso picasso) {
        this.context = context;
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
        this.picasso = picasso;
    }

    public PoetsRecyclerView(Fragment context, Values values, SparseBooleanArray sparseBooleanArray, Boolean multiSelection) {
        this.context = context;
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
        this.multiSelection = multiSelection;
        this.sparseBooleanArray = sparseBooleanArray;
    }

    @Override
    public PoetsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poet_single_element, parent, false);
        viewHolder = new ViewHolder(singleElement);
        return viewHolder;
    }

    private int getId(int position) {
        String adress = photos.get(position);
        if (adress != null)
            return context.getResources().getIdentifier(photos.get(position), "drawable", BuildConfig.APPLICATION_ID);
        else return 0;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("Multiselection :" + multiSelection);
        TextView authorName = holder.authorName;
        authorName.setText(names.get(position));
        int id = getId(position);
        if (id != 0) {
            System.out.println("load image from resources");
            Glide.with(context).load(id).centerCrop().into(holder.imageView);
        } else {
            System.out.println("load image from sdcard");
            String adress = photos.get(position);

            //todo getDrawable deprecated but i have no other for my api lvl
            Glide.with(context).load("file:///" + adress).placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_app)).centerCrop() // resizes the image to these dimensions (in pixel)
                    .into(holder.imageView);
        }
        setViewSelected(holder, position);
    }

    private void setViewSelected(ViewHolder viewHolder, int position) {
       //System.out.println("Selected boolean array: " + sparseBooleanArray.toString() + " position: " + position);
        if (sparseBooleanArray.get(position)) {
           // System.out.println("Make Selected: " + sparseBooleanArray.get(position));
            viewHolder.mLayout.setCardBackgroundColor(context.getResources().getColor(R.color.selected));
        } else {
           // System.out.println("Make Selected: " + sparseBooleanArray.get(position));
            viewHolder.mLayout.setCardBackgroundColor(Color.WHITE);
        }
    }

    private void setSelection(int position) {
        ((PoetsFragment) context).updateSelection(position);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public CardView mLayout;
        public ImageView imageView;
        public TextView authorName;
        public Toolbar toolbar;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.poem_author_portrait);
            authorName = (TextView) v.findViewById(R.id.poem_author);

            mLayout = (CardView) v;
            mLayout.setOnClickListener(this);
            mLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //replaceToPoems();

            if (!((PoetsFragment) context).getMultiSelection())
                replaceToPoems();
            else setSelection(getAdapterPosition());
        }

        private void replaceToPoems() {
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context.getContext()).getSupportFragmentManager().beginTransaction();
            PoemsFragment poemsFragment = new PoemsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_AUTHOR, names.get(getAdapterPosition()));
            poemsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frame_root, poemsFragment);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @Override
        public boolean onLongClick(View v) {
           /* *
             * todo удалить после тестирования*/
            //toggleSelecction((CardView) v);
            System.out.println("setMultiSelection");
            if(!((PoetsFragment) context).getMultiSelection())
            setMultiSelectionMode(true);
            else setMultiSelectionMode(false);
            return true;
        }

        private void setMultiSelectionMode(Boolean multiSelectionMode) {
            ((PoetsFragment) context).setMultiSelection(multiSelectionMode, getAdapterPosition());
        }
    }
}
