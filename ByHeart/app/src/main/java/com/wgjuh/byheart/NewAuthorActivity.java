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
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {

                    System.out.println("ERRRROR: " + exception);
                    exception.printStackTrace();
                }
            });

            Picasso picasso = builder.build();
            picasso.load(new File(authorImagePath)).error(R.drawable.favorites)
                    .fit()
                    .centerCrop()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            System.out.println("ERROR");
                        }
                    });
        }
    }
}
