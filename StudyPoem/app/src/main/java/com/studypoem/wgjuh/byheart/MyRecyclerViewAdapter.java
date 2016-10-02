package com.studypoem.wgjuh.byheart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    private Map<Integer,Map<String,Integer>> map;
    private String[] titles;
    private Context context;
    private static boolean isFromDefaultLib = true;
    static int last_position = -1;
    static int current_position = -1;
    private ViewGroup parent;
    private ViewHolder viewHolder;

    MyRecyclerViewAdapter(Values values, int[] progress, Context context, Boolean isFromDefaultLib) {
        this.strings = values.getStrings();
        this.ids = values.getIds();
        this.context = context;
        this.isFromDefaultLib = isFromDefaultLib;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_poems_single_element, parent, false);
        viewHolder = new ViewHolder( v);
        this.parent = parent;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ListPoems.lvl >= 1) {
            ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
            layoutParams.weight = 2;
            ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setLayoutParams(layoutParams);
            ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setGravity(Gravity.CENTER);
            ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setMaxLines(2);
            ((ImageButton)holder.mLayout.findViewById(R.id.button_favorite)).setOnClickListener(this);
            String author =((Toolbar)parent.getRootView().findViewById(R.id.toolbar_list_poems)).getTitle().toString();
            String title = strings.get(position);
            if(new SQLWorker(context).isStarred(author,title)){
                System.out.println(MainActivity.TAG+ " button ON" );
                ((ImageButton)holder.mLayout.findViewById(R.id.button_favorite)).setImageResource(android.R.drawable.btn_star_big_on);
            }else {
                System.out.println(MainActivity.TAG+ " button OFF" );
                ((ImageButton)holder.mLayout.findViewById(R.id.button_favorite)).setImageResource(android.R.drawable.btn_star_big_off);
            }
        }
        else {
            int idPortrait;
            ((ImageButton)holder.mLayout.findViewById(R.id.button_favorite)).setVisibility(View.GONE);
            switch (strings.get(position)){
                case "Пушкин А.С.":
                    idPortrait = context.getResources().getIdentifier("pushkin","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Фет А.":
                    idPortrait = context.getResources().getIdentifier("fet","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Федор Т.":
                    idPortrait = context.getResources().getIdentifier("tyutchev","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Некрасов Н.":
                    idPortrait = context.getResources().getIdentifier("nekrasov","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Лермонтов М.Ю.":
                    idPortrait = context.getResources().getIdentifier("lermontov_2","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Иоганн Вольфганг фон Гёте":
                    idPortrait = context.getResources().getIdentifier("goethe","drawable",BuildConfig.APPLICATION_ID);
                    break;
                case "Есенин С.А.":
                    idPortrait = context.getResources().getIdentifier("esenin","drawable",BuildConfig.APPLICATION_ID);
                    break;
                default:
                    idPortrait = -1;
                    break;
            }
            if (idPortrait != -1)
            ((ImageView) holder.mLayout.findViewById(R.id.poem_author_portrait)).setImageResource(R.mipmap.ic_launcher);
        }
        ((TextView) holder.mLayout.findViewById(R.id.poem_author)).setText(strings.get(position));
        current_position = position;
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public void onClick(View v) {
        System.out.println(MainActivity.TAG + " parent : " + v.getParent().getParent().getParent());
        String author = ((Toolbar)v.getRootView().findViewById(R.id.toolbar_list_poems)).getTitle().toString();
        String title =((TextView)((LinearLayout)v.getParent()).findViewById(R.id.poem_author)).getText().toString();
       if(new SQLWorker(context).setStar(author,title) == 1){
           System.out.println(MainActivity.TAG+ " button ON" );
           ((ImageButton)v).setImageResource(android.R.drawable.btn_star_big_on);
       }else {
           System.out.println(MainActivity.TAG+ " button OFF" );
           ((ImageButton) v).setImageResource(android.R.drawable.btn_star_big_off);
           if(author.equals("Starred")) {
               strings.clear();
               strings.addAll(new SQLWorker(context).getStarred().getStrings());
               notifyItemRemoved(parent.indexOfChild((View) v.getParent().getParent().getParent()));
           }
       }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            if(isFromDefaultLib)
            v.setOnLongClickListener(this);
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

        @Override
        public boolean onLongClick(View v) {
            final View temp = v;
            System.out.println(MainActivity.TAG + " LONG PRESS");
            new AlertDialog.Builder(context).setTitle("Удалить запись?")
                    .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println(MainActivity.TAG + " DELETE");

                            String author = ((Toolbar) temp.getRootView().findViewById(R.id.toolbar_list_poems)).getTitle().toString();
                            String title = ((TextView) temp.findViewById(R.id.poem_author)).getText().toString();
                            new SQLWorker(context).removeRow(author, title);
                            strings.clear();
                            strings.addAll(new SQLWorker(context).getStringsFromDB(author).getStrings());
                            notifyItemRemoved(getAdapterPosition());                        }
                    }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).create().show();
            return true;
        }
    }

    @NonNull
    private Intent getIntentStudyPoem(View v) {
        int itemPosition = ((RecyclerView) v.getParent()).indexOfChild(v);
       // String[] titles = strings ;
        int positionInMass = 0;
        if(strings.size() != 0)
        for (String s : strings) {
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
