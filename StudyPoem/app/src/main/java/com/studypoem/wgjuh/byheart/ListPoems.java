package com.studypoem.wgjuh.byheart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by WGJUH on 20.09.2016.
 */


public class ListPoems extends CustomList {
    ImageButton imageButton;
    private Menu menu;
    public static final String ListTag = " ListPoemsTag ";
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainActivity.TAG+" ListPoemsTag " + " ListPoems OnCreate");
        setContentView(R.layout.list_poems);
        init();
    }
    @Override
    public Values getValues() {
        System.out.println(MainActivity.TAG+" ListPoemsTag " + " BUNDLE: " + bundle.getString(KEY_REGEX, null));
        return sqlWorker.getPoemsTitlesFromDB(bundle.getString(KEY_REGEX, null));
    }

    @Override
    public void initViews() {
        toolbar = null;
        toolbar = ((Toolbar) findViewById(R.id.toolbar_list_poems));
        recyclerView = (RecyclerView) findViewById(R.id.recyler_list_poems);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void init(){
        initViews();
        sqlWorker = new SQLWorker(this);
        mLayoutManager = new LinearLayoutManager(this);
        bundle = getIntent().getExtras();
        values = getValues();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, this, true);
        imageButton = (ImageButton) toolbar.findViewById(R.id.btn_back);
        System.out.println(MainActivity.TAG + " imgButton: " + imageButton);
        runSetters();
    }

    @Override
    public void makeDialog() {

    }

    @Override
    public void runSetters() {
        recyclerView.setLayoutManager(mLayoutManager);
        fab.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(bundle.getString(KEY_REGEX,null));
        Typeface khandBold = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setTypeface(khandBold);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        imageButton.setOnClickListener(this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onClick(View v) {
        lvl++;
        switch (v.getId()){
            case R.id.fab:
                Intent intent = new Intent(this, NewText.class);
                intent.putExtra(KEY_REGEX, toolbar.getTitle());
                intent.putExtra(KEY_NEW_TEXT, true);
                startActivityForResult(intent, 777);
                break;
            case R.id.btn_back:
                onBackPressed();
                break;
            default:
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        values.setStrings(getValues().getStrings());
        myRecyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_list_poems, menu);
        return true;
    }

    /**
     * Меняем иконку программно
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItem menuItem;
        float currentSize = 0;
        switch (item.getItemId()) {
            case R.id.action_add_favorites:
                menuItem = menu.findItem(R.id.action_add_favorites);
                menuItem.setIcon(R.drawable.favorite_active);
                break;
            case R.id.action_text_bigger:
                break;
            case R.id.action_text_smaller:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
