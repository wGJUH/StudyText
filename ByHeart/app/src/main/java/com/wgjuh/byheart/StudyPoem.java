package com.wgjuh.byheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.wgjuh.byheart.myapplication.R;

public class StudyPoem extends AppCompatActivity implements Data {
    private Bundle bundle;
    private SqlWorker sqlWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_poem);
        sqlWorker = new SqlWorker(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView)findViewById(R.id.study_text);
        setSupportActionBar(toolbar);
        textView.setText(getTextFromDB(getTextId()));
    }
    private int getTextId(){
        Intent intent = getIntent();
        if(intent != null) {
            bundle = getIntent().getExtras();
            if(bundle != null){
               return bundle.getInt(KEY_ID,ERROR);
            }
            else return ERROR;
        }else return ERROR;
    }
    private String getTextFromDB(int id){
        String text = sqlWorker.getTextFromDB(id);
        return text;
    }

}
