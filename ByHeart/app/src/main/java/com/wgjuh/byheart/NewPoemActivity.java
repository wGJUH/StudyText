package com.wgjuh.byheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.wgjuh.byheart.myapplication.R;

public class NewPoemActivity extends AppCompatActivity implements View.OnClickListener, Data {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_save_author:
                Toast.makeText(this,"SAVE",Toast.LENGTH_SHORT).show();
                break;
            case R.id.author_photo:
                Toast.makeText(this,"Photo",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
