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

import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
        setFont(editText);
        imageView.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    public void selectAuthorPortrait() {
        System.out.println("TEST");
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_LOAD_IMAGE);
    }

    public void setFont(EditText editText) {
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        editText.setTypeface(robotoslab);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author_photo:
                selectAuthorPortrait();
                break;
            case R.id.fab_save_author:
                if (saveNewAuthor()) {
                    setResult(RESULT_OK,getIntent().putExtra(KEY_AUTHOR,getAuthorName()));
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private boolean saveNewAuthor() {
        if (isNameNotNull()) {
            new SqlWorker(this).addNewAuthor(getAuthorName(), getAuthorImagePath());
            return true;
        } else return false;
    }

    private boolean isNameNotNull() {
        return !editText.getText().toString().equals("");
    }

    private String getAuthorName() {
        return editText.getText().toString();
    }

    private String getAuthorImagePath() {
        return authorImagePath;
    }

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
            System.out.println(" imagePath: " + authorImagePath);
            cursor.close();
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmap = decodeFile(new File(authorImagePath));
            System.out.println(" width :" + bitmap.getWidth() + " height: " + bitmap.getHeight());
            imageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 256;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE ||
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

}
