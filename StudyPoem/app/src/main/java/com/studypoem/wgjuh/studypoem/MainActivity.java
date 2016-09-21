package com.studypoem.wgjuh.studypoem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView defaultLibStart;
    CardView myLibStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultLibStart = (CardView) findViewById(R.id.defaultLibraryStart);
        myLibStart = (CardView) findViewById(R.id.myLibraryStart);
        defaultLibStart.setOnClickListener(this);
        myLibStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.defaultLibraryStart:
                 intent = new Intent(this,ListPoems.class);
                intent.putExtra("activity","defaulLibrary");
                startActivity(intent);
                break;
            case R.id.myLibraryStart:
                 intent = new Intent(this,ListPoems.class);
                intent.putExtra("activity","myLibrary");
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
