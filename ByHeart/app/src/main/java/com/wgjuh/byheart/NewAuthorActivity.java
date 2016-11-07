package com.wgjuh.byheart;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
    CustomTextView hint_photo;
    EditText editText;
    CustomTextView customTextView;
    String authorImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_author);
        customTextView = (CustomTextView) findViewById(R.id.toolbar_title);
        hint_photo = (CustomTextView)findViewById(R.id.hint_add_author_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle();
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
    private void setToolbarTitle(){
        customTextView.setText(this.getString(R.string.title_activity_new_author));
    }
    public void requestMultiplePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  && ContextCompat
                .checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Data.PERMISSION_REQUEST_CODE && grantResults.length == 2){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.permissions)).setMessage(getString(R.string.permissions_message))
                        .setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(),getString(R.string.thanks),Toast.LENGTH_SHORT).show();
                                requestMultiplePermissions();
                                dialog.cancel();
                            }
                        }).setNegativeButton(getString(R.string.denied), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(),getString(R.string.permissions_need),Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                }).create().show();
            }

        }else selectAuthorPortrait();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.author_photo:
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    requestMultiplePermissions();
                }else  selectAuthorPortrait();

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
            hint_photo.setVisibility(View.GONE);
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
                    .into(imageView);

        }
    }
}
