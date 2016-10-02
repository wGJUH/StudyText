package com.studypoem.wgjuh.byheart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "LearnTextByHeart";
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.defaultLibraryStart:
                ListPoems.lvl = 0;
                intent = new Intent(this,ListPoems.class);
                intent.putExtra("activity","defaulLibrary");
                startActivity(intent);
                break;
            case R.id.myLibraryStart:
                ListPoems.lvl = 1;
                intent = new Intent(this,ListPoems.class);
                intent.putExtra("activity","myLibrary");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
