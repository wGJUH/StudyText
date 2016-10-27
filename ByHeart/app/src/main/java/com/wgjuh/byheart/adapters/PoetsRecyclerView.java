package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.fragments.FavoriteFragment;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.myapplication.BuildConfig;
import com.wgjuh.byheart.myapplication.R;

import java.io.File;
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

    public PoetsRecyclerView(Context context, Values values) {
        this.context = context;
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
    private int getId(int position) {
        return context.getResources().getIdentifier(photos.get(position), "drawable", BuildConfig.APPLICATION_ID);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView authorName = (TextView) holder.mLayout.findViewById(R.id.poem_author);
        ImageView imageView = ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait));

        setTypeFace(authorName);
        authorName.setText(names.get(position));
        int id = getId(position);
        /**
         * todo вынести в отдельный поток
         *
         */
        if (id != 0)
            Picasso.with(context).load(id).into(imageView);
        else
            Picasso.with(context).load(new File(photos.get(position))).into(imageView);
    }
    private void setTypeFace(TextView textView){
        Typeface robotoslab = Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }
    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public Toolbar toolbar;
        public ViewHolder(View v) {
            super(v);
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
            /**
             * todo удалить после тестирования
             */
           // v.setSelected(!v.isSelected());
            return true;
        }
    }
}
