package com.studypoem.wgjuh.byheart;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

/**
 * Created by wGJUH on 07.10.2016.
 */

public class NewText extends AppCompatActivity implements Data, View.OnClickListener {
    private FloatingActionButton fab_random_hide;
    private FloatingActionButton fab_random_show;
    private FloatingActionButton fab;
    private Bundle bundle;
    private Toolbar toolbar;
    private EditText editText;
    private SQLWorker sqlWorker;
    private int mainFabState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(MainActivity.TAG + " NewTextActivity" );
        setContentView(R.layout.activity_study_poem);
        init();
    }

    private void changeViews() {
        System.out.println(MainActivity.TAG + " NEW TEXT");
        fab_random_hide.setVisibility(View.GONE);
        fab_random_show.setVisibility(View.GONE);
        String s = bundle.getString(KEY_REGEX);
        toolbar.setTitle(s);
        fab.setImageResource(R.drawable.ic_content_paste_white_48dp);
        ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.switcher);
        viewSwitcher.showNext();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.study_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_random_hide = (FloatingActionButton) findViewById(R.id.fab_text_hide);
        fab_random_show = (FloatingActionButton) findViewById(R.id.fab_text_show);
        editText = ((EditText) findViewById(R.id.new_text));
    }
    private void runSetters(){
        fab.setOnClickListener(this);
    }

    private void addChangeTextListner(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(fab).rotation(360f).withLayer().setDuration(2000).setInterpolator(interpolator).start();
                    fab.setImageResource(android.R.drawable.ic_menu_save);
                    mainFabState = SAVE_TEXT;
                } else {
                    System.out.println(MainActivity.TAG + " NEW TEXT from TextWatcher ");
                    final OvershootInterpolator interpolator = new OvershootInterpolator();
                    ViewCompat.animate(fab).rotation(-360f).withLayer().setDuration(2000).setInterpolator(interpolator).start();
                    fab.setImageResource(R.drawable.ic_content_paste_white_48dp);
                    mainFabState = NEW_TEXT;
                }
            }
        });
    }
    private void addNewText(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        if(clipboardManager.getPrimaryClip() != null) {
            ViewCompat.animate(fab).rotation(360f).withLayer().setDuration(3000).setInterpolator(interpolator).start();
            ((EditText) findViewById(R.id.new_text)).setText(clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(getBaseContext()));
            fab.setImageResource(android.R.drawable.ic_menu_save);
            mainFabState = SAVE_TEXT;
        }else if (((EditText) findViewById(R.id.new_text)).getText().length() != 0){
            ViewCompat.animate(fab).rotation(360f).withLayer().setDuration(3000).setInterpolator(interpolator).start();
            fab.setImageResource(android.R.drawable.ic_menu_save);
            mainFabState = SAVE_TEXT;
        }else{
            Toast.makeText(this,getString(R.string.toast_input_text), Toast.LENGTH_LONG).show();
        }
    }
    private void saveText(){
        final View view = LayoutInflater.from(this).inflate(R.layout.new_author, null, false);
        final String poem = ((EditText)findViewById(R.id.new_text)).getText().toString()+"\n";
        view.findViewById(R.id.photo_layout).setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.author_name_message)).setText(getResources().getString(R.string.input_title));
        ((EditText)view.findViewById(R.id.author_name)).setHint(getResources().getString(R.string.input_title_hint));
        new AlertDialog.Builder(this).setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String author_name = ((Toolbar)findViewById(R.id.study_toolbar)).getTitle().toString();
                        String poem_title = ((EditText)view.findViewById(R.id.author_name)).getText().toString();
                        System.out.println(MainActivity.TAG+ " author: " + author_name + " title: " + poem_title);
                        if(!poem_title.equals("")) {
                            if (author_name.equals(getString(R.string.title_favorites))){
                                sqlWorker.addStringToDB(null, poem_title, poem,true);
                                setResult(666);
                                finish();
                            }else{
                                sqlWorker.addStringToDB(author_name, poem_title, poem,false);
                                ListPoems.lvl--;
                                setResult(777);
                                finish();}
                        }else{
                            Toast.makeText(getBaseContext(),R.string.input_title,Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void init() {
        sqlWorker = new SQLWorker(this);
        bundle = getIntent().getExtras();
        initViews();
        changeViews();
        runSetters();
        addChangeTextListner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                switch (mainFabState){
                    case NEW_TEXT:
                        addNewText();
                        break;
                    case SAVE_TEXT:
                        saveText();
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
