package com.wgjuh.byheart;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.wgjuh.byheart.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Bitmap.createScaledBitmap;

public class NewAuthorActivity extends AppCompatActivity implements View.OnClickListener, Data {
    ImageView imageView;
    EditText editText;
    String authorImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_author);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save_author);
        imageView = (ImageView) findViewById(R.id.author_photo);
        editText = (EditText) findViewById(R.id.new_author_name);
        imageView.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    public void selectAuthorPortrait() {
        System.out.println("TEST");
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_LOAD_IMAGE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author_photo:
                selectAuthorPortrait();
                break;
            case R.id.fab_save_author:
                long result = saveNewAuthor();
                if (result != -1L) {
                    setResult(RESULT_OK,getIntent().putExtra(KEY_AUTHOR,getAuthorName()).putExtra(KEY_ID,result));
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private long saveNewAuthor() {
        if (isNameNotNull() && isAuthorNotExists(getAuthorName())) {
            return new SqlWorker(this).addNewAuthor(getAuthorName(), getAuthorImagePath());
        } else return -1L;
    }

    private boolean isAuthorNotExists(String authorName) {
        boolean result = new SqlWorker(this).isAuthorExist(authorName);
        if(!result){
            Toast.makeText(this,getString(R.string.author_exist),Toast.LENGTH_LONG).show();
        }
        return result;
    }

    private boolean isNameNotNull() {
        boolean result = !editText.getText().toString().equals("");
        if(!result){
            Toast.makeText(this,getString(R.string.hint_input_author_name),Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private String getAuthorName() {
        return editText.getText().toString();
    }

    private String getAuthorImagePath() {
        System.out.println("author path: " + authorImagePath);
        return authorImagePath;
    }

    /**
     * TODO сделать проверку на null !!!
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOAD_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            authorImagePath = cursor.getString(columnIndex); // returns null
            System.out.println("imagePath: " + authorImagePath);
            cursor.close();
            Glide.with(this)
                    .load(new File(authorImagePath))
                    .placeholder(this.getResources().getDrawable(R.drawable.ic_launcher_app))
                    .centerCrop()
                    .into(imageView);
        }
    }
}
