package com.studypoem.wgjuh.byheart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import static android.R.attr.bitmap;

/*Created by WGJUH on 20.09.2016.*/


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements View.OnClickListener, Data {
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    private ArrayList<String> portrait_ids;
    private String[] titles;
    private Context context;
    private static boolean isFromDefaultLib = true;
    static int last_position = -1;
    static int current_position = -1;
    private ViewGroup parent;
    private ViewHolder viewHolder;
    MyRecyclerViewAdapter(Values values, Context context, Boolean isFromDefaultLib) {
        this.strings = values.getStrings();
        this.ids = values.getIds();
        this.portrait_ids = values.getPortraitIds();
        this.context = context;
        this.isFromDefaultLib = isFromDefaultLib;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if(context instanceof ListPoems)
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poems_single_element, parent, false);
        else
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poet_single_element, parent, false);
        viewHolder = new ViewHolder(v);
        this.parent = parent;
        return viewHolder;
    }


    private void setStar(ViewHolder holder, int position) {

        String author = ((TextView)((Toolbar) parent.getRootView().findViewById(R.id.toolbar_list_poems)).findViewById(R.id.toolbar_title)).getText().toString();
        String title = strings.get(position);
        System.out.println(MainActivity.TAG + " SetStar " + " author: " + author + " title: " + title);
        if (new SQLWorker(context).isStarred(author, title)) {
            System.out.println(MainActivity.TAG + " button ON");
            ((ImageButton) holder.mLayout.findViewById(R.id.button_favorite)).setImageResource(R.drawable.favorite_active);
        } else {
            System.out.println(MainActivity.TAG + " button OFF");
            ((ImageButton) holder.mLayout.findViewById(R.id.button_favorite)).setImageResource(R.drawable.favorite_disabled);
        }
    }
    private String getTitle(){
        return ((TextView)viewHolder.mLayout.findViewById(R.id.toolbar_title)).getText().toString();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable drawable;
        Bitmap bitmap;
        int id = 0;
        if (context instanceof ListPoems) {
             holder.mLayout.findViewById(R.id.button_favorite).setOnClickListener(this);
            setStar(holder, position);
        } else {
            if (portrait_ids.get(position) != null) {
                id = context.getResources().getIdentifier(portrait_ids.get(position), "drawable", BuildConfig.APPLICATION_ID);
                if (id != 0) {
                    int value;
                    drawable = context.getResources().getDrawable(id);
                    /**
                     * todo for future if i will need resize my drawables
                     */
                    ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageDrawable(drawable);
                } else if ((bitmap = loadImageFromStorage(portrait_ids.get(position))) != null) {
                    ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageBitmap(bitmap);
                } else
                    ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageResource(R.mipmap.ic_launcher_app);
            } else
                ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageResource(R.mipmap.ic_launcher_app);
        }
        ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setText(strings.get(position));
        ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setTypeface(Typeface.createFromAsset(context.getAssets(), "robotoslab_regular.ttf"));
        current_position = position;
    }

    private Bitmap loadImageFromStorage(String path) {
        Bitmap b;
        try {
            File f = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return b;

    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public void onClick(View v) {
        System.out.println(MainActivity.TAG + " parent : " + v.getParent().getParent().getParent());
        String author = ((TextView)((Toolbar) v.getRootView().findViewById(R.id.toolbar_list_poems)).findViewById(R.id.toolbar_title)).getText().toString();        String title = ((TextView) ((LinearLayout) v.getParent()).findViewById(R.id.poem_author)).getText().toString();
        if (new SQLWorker(context).setStar(author, title) == 1) {
            System.out.println(MainActivity.TAG + " button ON");
            ((ImageButton) v).setImageResource(R.drawable.favorite_active);
        } else {
            System.out.println(MainActivity.TAG + " button OFF");
            ((ImageButton) v).setImageResource(R.drawable.favorite_disabled);
            if (author.equals(context.getString(R.string.title_favorites))) {
                strings.clear();
                strings.addAll(new SQLWorker(context).getStarred().getStrings());
                notifyItemRemoved(parent.indexOfChild((View) v.getParent().getParent().getParent()));
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public Toolbar toolbar;
        public TextView toolbar_title;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            if (!(context instanceof ListFavorites)) v.setOnLongClickListener(this);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if (context instanceof ListPoems) {
                /*System.out.println(MainActivity.TAG + " context is instance of ListPoems");
                intent = getIntentStudyPoem(v);
                ListPoems.lvl++;
                v.getContext().startActivity(intent);*/
            } else {
                System.out.println(MainActivity.TAG + " context is instance of ListPoets");
                intent = new Intent(context, ListPoems.class);
                intent.putExtra(KEY_REGEX, ((TextView) v.findViewById(R.id.poem_author)).getText().toString());
                ListPoems.lvl++;
                v.getContext().startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View v) {
            /**
             * todo удалить после тестирования
             */
            v.setSelected(!v.isSelected());


            /*final View temp = v;
            System.out.println(MainActivity.TAG + " LONG PRESS");
            new AlertDialog.Builder(context).setTitle(context.getString(R.string.delete))
                    .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println(MainActivity.TAG + " DELETE");
                            String author = ((Toolbar) temp.getRootView().findViewById(R.id.toolbar_list_poems)).getTitle().toString();
                            String title = ((TextView) temp.findViewById(R.id.poem_author)).getText().toString();
                            new SQLWorker(context).removeRow(author, title);
                            if (context instanceof ListPoems) {
                                strings.clear();
                                strings.addAll(new SQLWorker(context).getStringsFromDB(author).getStrings());
                            } else {
                                Values values = new SQLWorker(context).getPoemsAuthorsFromDB();
                                strings.clear();
                                strings.addAll(values.getStrings());
                                ids.clear();
                                ids.addAll(values.getIds());
                                portrait_ids.clear();
                                portrait_ids.addAll(values.getPortraitIds());
                            }
                            notifyItemRemoved(getAdapterPosition());
                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();*/
            return true;
        }
    }

    @NonNull
    private Intent getIntentStudyPoem(View v) {
        int itemPosition = ((RecyclerView) v.getParent()).indexOfChild(v);
        int positionInMass = 0;
        if (strings.size() != 0)
            for (String s : strings) {
                if (s.equals(((TextView) v.findViewById(R.id.poem_author)).getText()))
                    break;
                positionInMass++;
            }
        System.out.println("Clicked and Position isViewHolder " + itemPosition + " last position" + last_position);
        Intent intent = new Intent(v.getContext(), StudyPoem.class);
        if (last_position != itemPosition) {
            intent.putExtra(KEY_LAST_POSITION, positionInMass);
        }
        intent.putExtra(KEY_LAST_POSITION, last_position);
        intent.putExtra(KEY_REGEX, ((TextView) v.findViewById(R.id.poem_author)).getText().toString());
        intent.putExtra(KEY_AUTHOR, ((Toolbar) parent.getRootView().findViewById(R.id.toolbar_list_poems)).getTitle().toString());

        intent.putExtra(KEY_FROM_LIBRARY, isFromDefaultLib);
        last_position = itemPosition;
        return intent;
    }
}
