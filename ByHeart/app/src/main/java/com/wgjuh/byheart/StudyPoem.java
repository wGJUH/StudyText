package com.wgjuh.byheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.wgjuh.byheart.adapters.TextRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyPoem extends AppCompatActivity implements Data, View.OnClickListener, AbsListView.OnScrollListener {
    private Bundle bundle;
    private SqlWorker sqlWorker;
    private ArrayList<SpannableStringBuilder> spannableStringBuilders;
    private RecyclerView recyclerView;
    private  TextRecyclerView textRecyclerView;
    private FloatingActionButton floatingActionMain,fab1;
    private int first_visible;
    public FloatingActionMenu floatingActionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_poem);
        floatingActionMenu = (FloatingActionMenu)findViewById(R.id.fab_menu);

        sqlWorker = new SqlWorker(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitleFromDB(getTextId()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        spannableStringBuilders = new ArrayList<>();
        spannableStringBuilders.addAll(getArray(getSpannableString(getTextFromDB(getTextId()))));
        /*recyclerView = (RecyclerView)findViewById(R.id.text_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new TextRecyclerView(this,spannableStringBuilders));*/
        ListView listView = (ListView)findViewById(R.id.text_recycler_view);
        textRecyclerView = new TextRecyclerView(this,spannableStringBuilders);
        listView.setOnScrollListener(this);
        listView.setAdapter(textRecyclerView);

    }

    private int getTextId() {
        Intent intent = getIntent();
        if (intent != null) {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                return bundle.getInt(KEY_ID, ERROR);
            } else return ERROR;
        } else return ERROR;
    }

    private String getTextFromDB(int id) {
        return sqlWorker.getTextFromDB(id);
    }
    private String getTitleFromDB(int id) {
        return sqlWorker.getTitleFromDB(id);
    }

    private SpannableString getSpannableString(String s) {
        SpannableString text = new SpannableString(s);
        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(s);
        while (matcher.find())
            text.setSpan(new SpannableByHeart(this, matcher.start(), matcher.end()), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    private ArrayList<SpannableStringBuilder> getArray(SpannableString spannableString) {
        ArrayList<SpannableStringBuilder> rowArray = new ArrayList<SpannableStringBuilder>();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Matcher matcher = Pattern.compile(PATTERN_STRING).matcher(spannableString);
        int startPosition = 0;
        while (matcher.find()) {
            if (matcher.start() - startPosition >= 1) {
                spannableStringBuilder.append(spannableString.subSequence(startPosition, matcher.end() - 1));
                rowArray.add(spannableStringBuilder);
                spannableStringBuilder = new SpannableStringBuilder();
            }
            startPosition = matcher.end();
        }
        return rowArray;
    }
    public void updateStringsArray(int position, SpannableStringBuilder spannable){
        spannableStringBuilders.remove(position);
        spannableStringBuilders.add(position, spannable);
        textRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_study_poem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!floatingActionMenu.isMenuHidden()) {
            System.out.println("isShown");
            floatingActionMenu.close(true);
        }

        /*
        if (firstVisibleItem > first_visible) {
            System.out.println("smaller");
            if (!floatingActionMenu.isMenuHidden()) {
                System.out.println("isShown");
                floatingActionMenu.close(true);
            }
        } else if (firstVisibleItem < first_visible) {
            System.out.println("bigger");
            if (!floatingActionMenu.isMenuHidden()) {
                System.out.println("isHided");
                floatingActionMenu.close(true);
            }
        }
        first_visible = firstVisibleItem;*/
    }
}
