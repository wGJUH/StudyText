package com.studypoem.wgjuh.studypoem;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import java.io.FileWriter;
import java.util.Arrays;

/**
 * Created by WGJUH on 20.09.2016.
 */
public class ListPoems extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_poems);
        recyclerView = (RecyclerView)findViewById(R.id.recyler_list_poems);
        int[] progress = {0};
        String[] poets = getResources().getStringArray(R.array.poets);
        String[] titles = getResources().getStringArray(R.array.titles);
        System.out.println("Poets: " + Arrays.toString(poets) + " titles: " + Arrays.toString(titles) );
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(poets,titles,progress, this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        ((FloatingActionButton)findViewById(R.id.fab)).setVisibility(View.INVISIBLE);
    }

    private boolean checkIfStandartPoemsExist(){

        return false;
    }

 /*   private void fileWriter(String filename){
        FileWriter fileWriter = null;
        try {

        }catch (Ex)

    }
*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
