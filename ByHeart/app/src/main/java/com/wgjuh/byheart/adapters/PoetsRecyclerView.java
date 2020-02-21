package com.wgjuh.byheart.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.NewAuthorActivity;
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
    private Tracker tracker;


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
            Glide.with(context.getContext()).load(id).centerCrop().into(holder.imageView);
        } else {
            System.out.println("load image from sdcard");
            String adress = photos.get(position);

            //todo getDrawable deprecated but i have no other for my api lvl
            Glide.with(context.getContext()).load("file:///" + adress).placeholder(context.getResources().getDrawable(R.drawable.ic_launcher_app)).crossFade().centerCrop() // resizes the image to these dimensions (in pixel)
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
        public ImageButton imageButton;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.poem_author_portrait);
            authorName = (TextView) v.findViewById(R.id.poem_author);
            imageButton = (ImageButton)v.findViewById(R.id.button_options);

            mLayout = (CardView) v;
            imageButton.setOnClickListener(this);
            mLayout.setOnClickListener(this);
            mLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //replaceToPoems();
        switch (v.getId()){
            case R.id.button_options:
                System.out.println("OPTIONS");
                PopupMenu popupMenu = new PopupMenu(context.getContext(),imageButton);
                popupMenu.getMenuInflater().inflate(R.menu.menu_poet_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tracker.send(new HitBuilders.EventBuilder().setCategory("Action")
                                .setAction("Edit Folder").build());
                        Intent intent = new Intent(context.getContext(), NewAuthorActivity.class);
                        System.out.println("position: " + getAdapterPosition());
                        intent.putExtra(Data.KEY_AUTHOR, names.get(getAdapterPosition()));
                        intent.putExtra(Data.KEY_ID,photos.get(getAdapterPosition()));
                        context.getActivity().startActivityForResult(intent, REQUEST_ADD_NEW_AUTHOR);
                        return false;
                    }
                });
                popupMenu.show();
                break;
            default:
                if (!((PoetsFragment) context).getMultiSelection())
                    replaceToPoems();
                else setSelection(getAdapterPosition());
                break;
        }

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
            tracker.send(new HitBuilders.EventBuilder().setCategory("Action").setAction("MultiSelectionFolders").build());
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
