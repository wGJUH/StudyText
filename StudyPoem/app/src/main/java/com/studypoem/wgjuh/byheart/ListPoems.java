package com.studypoem.wgjuh.byheart;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class ListPoems extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "SQL_test";
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    public static int lvl = 0;
    SQLWorker sqlWorker;
    Values values;
    Bundle bundle;
    Toolbar toolbar;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lvl--;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_poems);
        sqlWorker = new SQLWorker(this);
        toolbar = ((Toolbar) findViewById(R.id.toolbar_list_poems));
        recyclerView = (RecyclerView) findViewById(R.id.recyler_list_poems);
        if (lvl < 1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mLayoutManager = new AuthorsGrid(this, 150 * metrics.densityDpi / 160);
        } else mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        bundle = getIntent().getExtras();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (bundle != null) {
            if (bundle.getString("activity").equals("defaulLibrary")) {
                int[] progress = {0};
                values = getValues();
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, progress, this, true);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                if(bundle.getString("regex", null) == null)
                toolbar.setTitle("Library");
                else
                    toolbar.setTitle(bundle.getString("regex", null));
            } else {
                int[] progress = {0};
                values = sqlWorker.getStarred();
                myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, progress, this, false);
                recyclerView.setAdapter(myRecyclerViewAdapter);
                if(bundle.getString("regex", null) == null)
                    toolbar.setTitle("Starred");
                else
                    toolbar.setTitle(bundle.getString("regex", null));

            }
        }
        setSupportActionBar(toolbar);
    }

    private  Values getValues(){
        System.out.println(MainActivity.TAG + " BUNDLE: " +bundle.getString("regex", null));
        return sqlWorker.getStringsFromDB(bundle.getString("regex", null));
    }

    @Override
    public void onClick(View v) {
        if(lvl < 1) {
            System.out.println("STRING_TEST01");
            final View view = LayoutInflater.from(this).inflate(R.layout.new_author, null, false);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);
            alertDialogBuilder.setCancelable(true).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String author_name = ((EditText)view.findViewById(R.id.author_name)).getText().toString();
                    if(!author_name.equals("")) {
                        sqlWorker.addStringToDB(author_name, null, null,false);
                        values.setStrings(getValues().getStrings());
                        System.out.println(MainActivity.TAG + " ITEMS: " + myRecyclerViewAdapter.getItemCount() + " VALUES: " + values.getStrings().toString());
                        myRecyclerViewAdapter.notifyItemInserted(sqlWorker.getRowNumber(author_name) - 1);
                    }
                }
            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialogBuilder.create().show();
        }else{
            lvl++;
                Intent intent = new Intent(this,StudyPoem.class);
                intent.putExtra("regex", toolbar.getTitle());
                intent.putExtra("new_text", true);
                startActivityForResult(intent,777);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(MainActivity.TAG + " code: " + resultCode);
        if(resultCode == 666) values.setStrings(sqlWorker.getStarred().getStrings());
        else values.setStrings(getValues().getStrings());

        myRecyclerViewAdapter.notifyDataSetChanged();
    }
}
