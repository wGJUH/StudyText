package com.wgjuh.byheart;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.github.clans.fab.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.wgjuh.byheart.myapplication.R;

public class NewPoemActivity extends AppCompatActivity implements View.OnClickListener, Data {
    EditText editTextTitle;
    EditText editTextPoem;
    Intent intent;
    Bundle bundle;
    String authorName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fabSaveText = (FloatingActionButton) findViewById(R.id.fab_save_new_text);
        FloatingActionButton fabPasteText = (FloatingActionButton) findViewById(R.id.fab_paste_new_text);
        FloatingActionMenu floatingActionMenu = (FloatingActionMenu)findViewById(R.id.fab_menu);
        floatingActionMenu.open(false);
        fabSaveText.setOnClickListener(this);
        fabPasteText.setOnClickListener(this);
        editTextPoem = (EditText)findViewById(R.id.new_text_poem);
        editTextTitle = (EditText)findViewById(R.id.new_text_title);
        setFont(editTextPoem);
        setFont(editTextTitle);
        getArguments();
        setViewpagerTitle();
    }
    private void getArguments(){
        intent = getIntent();
        if(intent != null){
            bundle = intent.getExtras();
            if(bundle != null){
                authorName = bundle.getString(KEY_AUTHOR,null);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_poem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_text_bigger:
                editTextPoem.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextPoem.getTextSize()+2);
                break;
            case R.id.action_text_smaller:
                editTextPoem.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextPoem.getTextSize()-2);
                break;
            default:
                break;
        }



        return super.onOptionsItemSelected(item);
    }
    private void setViewpagerTitle() {
        TextView viewTitle = (TextView)findViewById(R.id.toolbar_title);
        viewTitle.setText(authorName);
        setTypeFace(viewTitle);
    }
    private String getViewPagerTitle(){
        TextView viewTitle = (TextView)findViewById(R.id.toolbar_title);
        return viewTitle.getText().toString();
    }
    private void setTypeFace(TextView textView) {
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_save_new_text:
                if(!editTextTitle.getText().toString().equals("") && !editTextPoem.getText().toString().equals("")) {
                    saveNewText();
                    setResult(RESULT_OK, getIntent().putExtra(KEY_TITLE, getPoemTitle()));
                    finish();
                }
                else Toast.makeText(this,this.getString(R.string.toast_input_title_or_text),Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_paste_new_text:
                //Toast.makeText(this,"Paste",Toast.LENGTH_SHORT).show();
                pasteNewText();
                break;
            default:
                break;
        }
    }
    @NonNull
    public String getPoemTitle(){
        return editTextTitle.getText().toString();
    }
    @NonNull
    private String getPoem() {
        return editTextPoem.getText().toString()+"\n";
    }

    private void saveNewText(){
        new SqlWorker(this).addNewText(getViewPagerTitle(),getPoemTitle(), getPoem());
    }



    private void pasteNewText(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        CharSequence charSequence;
        if(clipData != null){
            charSequence = clipData.getItemAt(0).coerceToText(this);
            if(charSequence != null){
                editTextPoem.setText(charSequence);
            }
        }
    }
    public void setFont(EditText editText) {
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        editText.setTypeface(robotoslab);
    }
}
