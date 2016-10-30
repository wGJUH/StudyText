package com.wgjuh.byheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wgjuh.byheart.adapters.TextRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyPoem extends AppCompatActivity implements Data {
    private Bundle bundle;
    private SqlWorker sqlWorker;
    private ArrayList<SpannableStringBuilder> spannableStringBuilders;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_poem);
        sqlWorker = new SqlWorker(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //TextView textView = (TextView) findViewById(R.id.study_text);
        setSupportActionBar(toolbar);
        //textView.setText(getTextFromDB(getTextId()));
        spannableStringBuilders = new ArrayList<>();
        spannableStringBuilders.addAll(getArray(getSpannableString(getTextFromDB(getTextId()))));
        /*recyclerView = (RecyclerView)findViewById(R.id.text_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new TextRecyclerView(this,spannableStringBuilders));*/
        ListView listView = (ListView)findViewById(R.id.text_recycler_view);
        listView.setAdapter(new TextRecyclerView(this,spannableStringBuilders));
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

}
