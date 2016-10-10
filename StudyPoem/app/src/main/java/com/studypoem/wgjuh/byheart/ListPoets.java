package com.studypoem.wgjuh.byheart;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by WGJUH on 20.09.2016.
 */


public class ListPoets extends CustomList {
    View dialogView;
    String authoImagePath = null;
    String savePath;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        lvl--;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainActivity.TAG + " ListPoets OnCreate");
        setContentView(R.layout.list_poets);
        init();
    }
    @Override
    public void initViews(){
        toolbar = ((Toolbar) findViewById(R.id.toolbar_list_poems));
        recyclerView = (RecyclerView) findViewById(R.id.recyler_list_poems);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }
    private DisplayMetrics getMetrics(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
    @Override
    public Values getValues() {
        System.out.println(MainActivity.TAG + " ListPoets BUNDLE: " + bundle.getString(KEY_REGEX, null));
        return sqlWorker.getPoemsAuthorsFromDB();
    }

    @Override
    public void runSetters() {
        recyclerView.setLayoutManager(mLayoutManager);
        fab.setOnClickListener(this);
        recyclerView.setAdapter(myRecyclerViewAdapter);
       // toolbar.setTitle(getResources().getString(R.string.title_library));
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(getResources().getString(R.string.title_library));
        Typeface khandBold = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        ((TextView)toolbar.findViewById(R.id.toolbar_title)).setTypeface(khandBold);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
    @Override
    public void init(){
        initViews();
        sqlWorker = new SQLWorker(this);
        mLayoutManager = new AuthorsGrid(this, 172 * getMetrics().densityDpi / 160);
        bundle = getIntent().getExtras();
        values = getValues();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(values, this, true);
        runSetters();
    }
    @Override
    public void makeDialog(){
        dialogView = LayoutInflater.from(this).inflate(R.layout.new_author, null, false);
        authoImagePath = null;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(true).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String author_name = ((EditText) dialogView.findViewById(R.id.author_name)).getText().toString();
                if (!author_name.equals("")) {
                    if(authoImagePath != null){
                        savePath = saveToInternalStorage(decodeFile(new File(authoImagePath)),author_name);
                        System.out.println(MainActivity.TAG + " saved to: " + savePath);
                    }
                    sqlWorker.addStringToDB(author_name,savePath,null, null, false);
                    values.setAll(getValues());
                    myRecyclerViewAdapter.notifyItemInserted(sqlWorker.getRowNumber(author_name) - 1);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }
    private String saveToInternalStorage(Bitmap bitmapImage,String authorName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,authorName+".PNG");
        System.out.println(MainActivity.TAG + " myPath: " + mypath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getPath();
    }
    @Override
    public void onClick(View v) {
           makeDialog();
    }
    public void selectAuthorPortrait(View v){
        Toast.makeText(this,"clicked",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, LOAD_IMAGE);
    }
    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=256;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE ||
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOAD_IMAGE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            authoImagePath = cursor.getString(columnIndex); // returns null
            System.out.println(MainActivity.TAG + " imagePath: "+ authoImagePath);
            cursor.close();
            ImageView imageButton = (ImageView) dialogView.findViewById(R.id.author_icon);
            imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmap = decodeFile(new File(authoImagePath));
            System.out.println(MainActivity.TAG + " width :" + bitmap.getWidth() + " height: " + bitmap.getHeight());
            imageButton.setImageBitmap(bitmap);
            dialogView.findViewById(R.id.photo_hint).setVisibility(View.GONE);
        }
    }
}
