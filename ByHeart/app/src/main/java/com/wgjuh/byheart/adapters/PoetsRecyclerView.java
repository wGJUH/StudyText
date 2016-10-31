package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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
    private Context context;
    private Picasso picasso;
    public PoetsRecyclerView(Context context, Values values, Picasso picasso) {
        this.context = context;
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
        this.picasso = picasso;
    }
    public PoetsRecyclerView(Context context, Values values) {
        this.context = context;
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
    }
    @Override
    public PoetsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poet_single_element, parent, false);
        viewHolder = new ViewHolder(singleElement);
        return viewHolder;
    }
    private int getId(int position) {
        String adress = photos.get(position);
        if(adress != null)
        return context.getResources().getIdentifier(photos.get(position), "drawable", BuildConfig.APPLICATION_ID);
        else return 0;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView authorName = holder.authorName;
        //setTypeFace(authorName);
        authorName.setText(names.get(position));
        int id = getId(position);
     /*   *
         * todo разобраться с пикасо и проверить размеры
         **/
        if (id != 0){
            System.out.println("load image from resources");
            Glide.with(context).load(id).centerCrop().into(holder.imageView);
        /*    if (position + 1 < photos.size()) {
                id = getId(position + 1);
                if(id != 0)
                picasso.load(id).fetch();
            }*/
        }
        else {
            System.out.println("load image from sdcard");
            String adress = photos.get(position);
            //todo getDrawable deprecated but i have no other for my api lvl
            Glide.with(context).load("file:///" + adress).placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_app)).centerCrop() // resizes the image to these dimensions (in pixel)
                    .into(holder.imageView);
            /*if (position + 1 < photos.size()) {
                adress = photos.get(position + 1);
                picasso.load("file:///" + adress).fetch();
            }*/
        }
        /*if (id != 0){
            System.out.println("load image from resources");
            picasso.load(id).fit().centerCrop().noFade().into(holder.imageView);
        *//*    if (position + 1 < photos.size()) {
                id = getId(position + 1);
                if(id != 0)
                picasso.load(id).fetch();
            }*//*
        }
        else {
            System.out.println("load image from sdcard");
            String adress = photos.get(position);
            //todo getDrawable deprecated but i have no other for my api lvl
            picasso.load("file:///" + adress).fit().placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_app)).centerCrop() // resizes the image to these dimensions (in pixel)
                    .noFade().into(holder.imageView);
            *//*if (position + 1 < photos.size()) {
                adress = photos.get(position + 1);
                picasso.load("file:///" + adress).fetch();
            }*//*
        }*/
    }
  /*  private void setTypeFace(TextView textView){
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/
    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public ImageView imageView;
        public TextView authorName;
        public Toolbar toolbar;
        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView)v.findViewById(R.id.poem_author_portrait);
            authorName = (TextView)v.findViewById(R.id.poem_author);
            v.setOnClickListener(this);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {

           replaceToPoems();
        }
        private void replaceToPoems(){
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            PoemsFragment poemsFragment = new PoemsFragment();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_AUTHOR,names.get(getAdapterPosition()));
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

           // v.setSelected(!v.isSelected());
            return true;
        }
    }
}


/**
 * TODO it's work perfectly
 */
/*

public class PoetsRecyclerView extends ArrayAdapter {

    private ArrayList<String> photos;
    private ArrayList<String> names;
    private ArrayList<Integer> ids;
    private Picasso picasso;
    private View rootView;
    SpannableString spannableString;
    Context context;
    ArrayList<SpannableStringBuilder> strings;
    String s;
    static float setTextSize = 0;
    public PoetsRecyclerView(Context context, Values values) {
        super(context,R.layout.list_poet_single_element, values.getStrings());
        this.context = context;
        this.photos = values.getPortraitIds();
        this.names = values.getStrings();
        this.ids = values.getIds();
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
        picasso = builder.build();

    }

    private int getId(int position) {
        String adress = photos.get(position);
        if(adress != null)
            return context.getResources().getIdentifier(photos.get(position), "drawable", BuildConfig.APPLICATION_ID);
        else return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mLayoutInflator.inflate(R.layout.list_poet_single_element,null);
        ((TextView)rootView.findViewById(R.id.poem_author)).setText(names.get(position));
        ImageView imageView = (ImageView)rootView.findViewById(R.id.poem_author_portrait);
        int id = getId(position);
        if (id != 0)
            picasso.load(id).resize(256, 250).centerCrop().into(imageView);
        else {
            String adress = photos.get(position);
            //todo getDrawable deprecated but i have no other for my api lvl
            picasso.load("file:///" + adress).resize(256, 250).placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_app)).centerCrop() // resizes the image to these dimensions (in pixel)
                    .into(imageView);
        }
        return rootView;
    }
}*/
