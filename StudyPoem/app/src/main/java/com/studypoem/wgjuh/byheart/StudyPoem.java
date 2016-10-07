package com.studypoem.wgjuh.byheart;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.Duration;

public class StudyPoem extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, Data {
    public static Object tempString;
    //TODO подумать надо ли все время держать в памяти масив строк
    public  static ArrayList<SpannableStringBuilder> stringBuilders = new ArrayList<>();
    public  static Map<Integer, ArrayList<Integer>> map = new HashMap<>();
    private Context context;
    private ArrayAdapter arrayAdapter;
    public static Random random = new Random();
    private int first_visible = 0;
    private FloatingActionButton fab;
    private FloatingActionButton fab_random_hide;
    private FloatingActionButton fab_random_show;
    Bundle bundle;
    SQLWorker sqlWorker;
    private boolean isNewText;
    private boolean isNewTextShouldBeSave;
    private int mainFabState;
    private EditText editText;
    private Toolbar toolbar;

    StudyPoem(){}
    public void setSpannableString(int positionForView, SpannableStringBuilder spannableStringBuilder){
        stringBuilders.set(positionForView,spannableStringBuilder);
    }
    public void setMap(int position, int wordNumber){
        if (map.get(position).contains(wordNumber)) {
                map.get(position).remove(map.get(position).indexOf(wordNumber));
            } else map.get(position).add(wordNumber);
    }
    public void showSpannebleSize(){
        System.out.println(MainActivity.TAG + " size: " + stringBuilders.size());
    }
    @Override
    public void onBackPressed() {
        stringBuilders.clear();
        map.clear();
        if ((mainFabState == NEW_TEXT
                || mainFabState == SAVE_TEXT)
                && toolbar.getTitle().equals(FAVORITES)) {
            setResult(666);
        } else {
            ListPoems.lvl--;
        }
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println(MainActivity.TAG + " onRestoreInstance " );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_study_poem);
        init();
/*TODO поправить код получения строки*/
        System.out.println(MainActivity.TAG + " stringBuildersSize: " + stringBuilders.size());

        if(stringBuilders.size() == 0) {
            System.out.println(MainActivity.TAG + " regex string: " + bundle.getString(KEY_REGEX));
            String temp = sqlWorker.getPoemTextFromDB(bundle.getString(KEY_AUTHOR),bundle.getString(KEY_REGEX));
            stringBuilders.addAll(getArray(getSpannableString(temp)));
            for(int i = 0; i < stringBuilders.size();i++){
                map.put(i,new ArrayList<Integer>());
            }
            System.out.println(MainActivity.TAG + " StringBuilders size: " + stringBuilders.size());
            Log.d(MainActivity.TAG, "SIZE_ARRAY: " + stringBuilders.size());
        }
        arrayAdapter = new MyTextAdapter((StudyPoem) context, R.layout.list_view_test, R.id.my_custom_text, stringBuilders);
        ((ListView) findViewById(R.id.listView)).setAdapter(arrayAdapter);
        ((ListView) findViewById(R.id.listView)).setOnScrollListener(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.study_toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_random_hide = (FloatingActionButton) findViewById(R.id.fab_text_hide);
        fab_random_show = (FloatingActionButton) findViewById(R.id.fab_text_show);
    }
    private void runSetters(){
        fab.setImageResource(R.drawable.ic_settings_backup_restore_white_48dp);

        fab_random_hide.setOnClickListener(this);
        fab_random_show.setOnClickListener(this);
        fab.setOnClickListener(this);
        setSupportActionBar(toolbar);
    }
    private void init() {
        initViews();
        bundle = getIntent().getExtras();
        sqlWorker = new SQLWorker(this);
        toolbar.setTitle(bundle.getString(KEY_REGEX,null));
        runSetters();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @NonNull
    private SpannableString getSpannableString(String s) {
        SpannableString text = new SpannableString(s);
        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(s);
        while (matcher.find()) {
            text.setSpan(new LoremIpsumSpan(this), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    private ArrayList<SpannableStringBuilder> getArray(SpannableString spannableString) {
        ArrayList<SpannableStringBuilder> spannableStrings = new ArrayList<>();
        //StringBuilder stringBuilder = new StringBuilder();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        Matcher matcher = Pattern.compile(PATTERN_STRING).matcher(spannableString);
        View view = LayoutInflater.from(this).inflate(R.layout.my_custom_textview, null);
        TextView textView = (TextView) view.findViewById(R.id.my_custom_text);
        int lines = 1;
        int start_position = 0;
/* TODO разобраться с коэфицентами*/


        while (matcher.find()) {
            if (matcher.start() - start_position >= 1) {
                stringBuilder.append(spannableString.subSequence(start_position, matcher.end() - 1));
                spannableStrings.add(stringBuilder);
                lines++;
                //Log.d(MainActivity.TAG, "BUILDER: STRING_TEST " + stringBuilder);
                lines = 1;
                stringBuilder = new SpannableStringBuilder();
            }
            start_position = matcher.end();
        }
        return spannableStrings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_study_poem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        float currentSize = 0;
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_text_bigger:
/*TODO поправить условия*/


                if (((TextView) ((ListView) findViewById(R.id.listView)).getChildAt(0)) != null) {
                    MyTextAdapter.updateSize(this, 2, ((TextView) ((ListView) findViewById(R.id.listView)).getChildAt(0)).getTextSize());
                    arrayAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.action_text_smaller:
                if (((TextView) ((ListView) findViewById(R.id.listView)).getChildAt(0)) != null) {
                    MyTextAdapter.updateSize(this, -2, ((TextView) ((ListView) findViewById(R.id.listView)).getChildAt(0)).getTextSize());
                    arrayAdapter.notifyDataSetChanged();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addNewText() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        if (clipboardManager.getPrimaryClip() != null) {
            ViewCompat.animate(fab).rotation(360f).withLayer().setDuration(3000).setInterpolator(interpolator).start();
            ((EditText) findViewById(R.id.new_text)).setText(clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(getBaseContext()));
            fab.setImageResource(android.R.drawable.ic_menu_save);
            isNewTextShouldBeSave = true;
            mainFabState = SAVE_TEXT;
        } else if (((EditText) findViewById(R.id.new_text)).getText().length() != 0) {
            ViewCompat.animate(fab).rotation(360f).withLayer().setDuration(3000).setInterpolator(interpolator).start();
            fab.setImageResource(android.R.drawable.ic_menu_save);
            isNewTextShouldBeSave = true;
            mainFabState = SAVE_TEXT;
        } else {
            Toast.makeText(this, "Please input or copy some text", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveText() {
        final View view = LayoutInflater.from(this).inflate(R.layout.new_author, null, false);
        final String poem = ((EditText) findViewById(R.id.new_text)).getText().toString() + "\n";
        new AlertDialog.Builder(this).setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String author_name = ((Toolbar) findViewById(R.id.study_toolbar)).getTitle().toString();
                        String poem_title = ((EditText) view.findViewById(R.id.author_name)).getText().toString();
                        System.out.println(MainActivity.TAG + " author: " + author_name + " title: " + poem_title);
                        if (!poem_title.equals("")) {
                            if (author_name.equals(FAVORITES)) {
                                sqlWorker.addStringToDB(null, poem_title, poem, true);
                                setResult(666);
                                finish();
                            } else {
                                sqlWorker.addStringToDB(author_name, poem_title, poem, false);
                                ListPoems.lvl--;
                                setResult(777);
                                finish();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), R.string.input_title, Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();
    }

    private void formateView() {
        stringBuilders.clear();
        int current_scroll = ((ListView) findViewById(R.id.listView)).getScrollY();
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        stringBuilders.addAll(getArray(getSpannableString(sqlWorker.getPoemTextFromDB(bundle.getString(KEY_AUTHOR),bundle.getString(KEY_REGEX)))));
        ((ListView) findViewById(R.id.listView)).setScrollY(current_scroll);
        arrayAdapter.notifyDataSetChanged();
        map.clear();
        for(int i = 0; i < stringBuilders.size();i++)map.put(i,new ArrayList<Integer>());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                formateView();
                break;
/* TODO доработать логику автоматического скрывания объектов*/


            case R.id.fab_text_hide:
                if (stringBuilders.size() != 0) {
                    for (int i = 0; i < stringBuilders.size(); i++) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilders.get(i));
                        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(spannableStringBuilder);
                        int count = 0;
                        while (matcher.find()) count++;
                        Log.d(MainActivity.TAG, "TEST_COUNT " + count + " string: " + i);
                        matcher.reset(spannableStringBuilder);
                        if (count != 0) {
                            int test = random.nextInt(count);
                            if (map.get(i) != null && map.get(i).size() != count) {
                                while (map.get(i).contains(test)) {
                                    Log.d(MainActivity.TAG, "TEST: " + test);
                                    test = random.nextInt(count);
                                }
                                map.get(i).add(test);
                            }
                            Log.d(MainActivity.TAG, "StartRandom: " + test + " string: " + i + " MAP: " + map.get(i).toString());
                            int j = 0;
                            while (matcher.find()) {
                                Log.d(MainActivity.TAG, "j: " + j);
                                if (j == test) {
                                    BackgroundColorSpan[] colorSpen = ((Spannable) stringBuilders.get(i)).getSpans(matcher.start(), matcher.end(), BackgroundColorSpan.class);
                                    Log.d(MainActivity.TAG, "SPANS_HIDe: " + colorSpen.length + " word: " + matcher.group());
                                    if (colorSpen.length != 0)
                                        stringBuilders.get(i).removeSpan(colorSpen[0]);
                                    stringBuilders.get(i).setSpan(new BackgroundColorSpan(Color.BLACK), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                }
                                j++;
                            }
                        } else map.put(i, new ArrayList<Integer>());
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.fab_text_show:

                for (int i = 0; i < map.keySet().size(); i++) {
                    System.out.println(MainActivity.TAG + " map.get(i): " + map.get(i));
                    if (map.get(i) != null) {
                        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(stringBuilders.get(i));
                        int j = 0;
                        Log.d(MainActivity.TAG, "j_remove: " + j + " map: " + map.get(i) + " key: " + i);
                        Log.d(MainActivity.TAG, "j_remove: " + j + " map: " + map.get(i).toString() + " key: " + i);
                        if (map.get(i).size() != 0) {

                            while (matcher.find()) {
                                if (j == map.get(i).get(map.get(i).size() - 1)) {
                                    BackgroundColorSpan[] colorSpen = ((Spannable) stringBuilders.get(i)).getSpans(matcher.start(), matcher.end(), BackgroundColorSpan.class);
                                    Log.d(MainActivity.TAG, "SPANS_SHOW: " + colorSpen.length + " word: " + matcher.group());
                                    if (colorSpen.length != 0)
                                        stringBuilders.get(i).removeSpan(colorSpen[0]);
                                    stringBuilders.get(i).setSpan(new BackgroundColorSpan(Color.WHITE), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                }
                                j++;
                            }
                            map.get(i).remove(map.get(i).size() - 1);
                        }
                    }

                    arrayAdapter.notifyDataSetChanged();
                }
                break;
            default:
                Log.d(MainActivity.TAG, "STRANGE_CLICK");
                break;

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(MainActivity.TAG, "SCROLL_STATE: " + scrollState);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > first_visible) {
            if (fab.isShown()) {
                fab.hide();
                fab_random_hide.hide();
                fab_random_show.hide();
            }
            Log.d(MainActivity.TAG, "on_Scroll: " + view.toString() + " first: " + firstVisibleItem);
        } else if (firstVisibleItem < first_visible) {
            if (!fab.isShown()) {
                fab.show();
                fab_random_show.show();
                fab_random_hide.show();
            }
        }
        first_visible = firstVisibleItem;
    }

}
