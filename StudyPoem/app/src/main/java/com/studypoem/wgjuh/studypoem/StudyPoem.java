package com.studypoem.wgjuh.studypoem;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyPoem extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {

    public static final String TAG = "STUDY_POEM";
    public static Object tempString;
    //TODO подумать надо ли все время держать в памяти масив строк
    public static ArrayList<SpannableStringBuilder> stringBuilders = new ArrayList<>();
    public static Map<Integer, ArrayList<Integer>> map = new HashMap<>();
    private Context context;
    private ArrayAdapter arrayAdapter;
    private int HideLevel = 0;
    public static Random random = new Random();
    private int first_visible = 0;
    private FloatingActionButton fab;
    private FloatingActionButton fab_random_hide;
    private FloatingActionButton fab_random_show;
    private boolean isPrevDefault = false;
    private ListPoems listPoems = new ListPoems();
    Bundle bundle;
    SQLWorker sqlWorker;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ListPoems.lvl--;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setContentView(R.layout.activity_study_poem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_random_hide = (FloatingActionButton) findViewById(R.id.fab_text_hide);
        fab_random_show = (FloatingActionButton) findViewById(R.id.fab_text_show);
        bundle = getIntent().getExtras();
        sqlWorker = new SQLWorker(this);
        /**
         * TODO check and chaneg this strange logic
         */
        if (bundle != null) {
            isPrevDefault = getIntent().getExtras().getBoolean("defaultPoems", false);
            new SQLWorker(this);
            String temp = sqlWorker.getStringsFromDB(bundle.getString("regex")).getStrings().get(0);
            System.out.println("STRING_STUDY: " + temp);
            if (isPrevDefault) {
                fab.setImageResource(R.drawable.ic_settings_backup_restore_white_48dp);
                stringBuilders.clear();
                map.clear();
//                Log.d(MainActivity.TAG,"TEXT: " + getResources().getStringArray(R.array.poems)[getIntent().getExtras().getInt("newText")]);
                stringBuilders.addAll(getArray(getSpannableString(temp)));
            } else {
                fab.setImageResource(R.drawable.ic_content_paste_white_48dp);
                stringBuilders.clear();
                map.clear();
  //              Log.d(MainActivity.TAG,"TEXT: " + getResources().getStringArray(R.array.poems)[getIntent().getExtras().getInt("newText")]);
                stringBuilders.addAll(getArray(getSpannableString(temp)));
            }
        }
        Log.d(MainActivity.TAG,"SIZE_ARRAY: " + stringBuilders.size());
        arrayAdapter = new MyTextAdapter((StudyPoem) context, R.layout.list_view_test, R.id.my_custom_text, stringBuilders);
        ((ListView) findViewById(R.id.listView)).setAdapter(arrayAdapter);
        ((ListView) findViewById(R.id.listView)).setOnScrollListener(this);
        /**
         *TODO поправить баг с выбором рандомных слов
         */
        fab_random_hide.setOnClickListener(this);
        fab_random_show.setOnClickListener(this);
        fab.setOnClickListener(this);

            }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @NonNull
    private SpannableString getSpannableString(String s) {
        SpannableString text = new SpannableString(s);
        Matcher matcher = Pattern.compile("[A-zA-Z0-9а-яА-ЯёЁ]+").matcher(s);
        while (matcher.find()) {
            text.setSpan(new LoremIpsumSpan(), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    private ArrayList<SpannableStringBuilder> getArray(SpannableString spannableString) {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        ArrayList<SpannableStringBuilder> spannableStrings = new ArrayList<>();
        //StringBuilder stringBuilder = new StringBuilder();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        Matcher matcher = Pattern.compile("\\n\\r|\\r\\n|\\r|\\n|\\Z").matcher(spannableString);
        View view = LayoutInflater.from(this).inflate(R.layout.my_custom_textview, null);
        TextView textView = (TextView) view.findViewById(R.id.my_custom_text);
        paint.setTypeface(textView.getTypeface());// your preference here
        paint.setTextSize(textView.getTextSize());// have this the same as your text size
        int textViewWidth = textView.getWidth();
        int lines = 1;
        int start_position = 0;
        int text_width = 0;
        /**
         * TODO разобраться с коэфицентами
         */
        while (matcher.find()) {
            if (matcher.start() - start_position >= 1) {
                // //Log.d(MainActivity.TAG,"СТРОКА: " + spannableString.subSequence(start_position, matcher.end()));
                stringBuilder.append(spannableString.subSequence(start_position, matcher.end() - 1));
                spannableStrings.add(stringBuilder);
                lines++;
//            }
//            if (lines >= 6) {
                Log.d(MainActivity.TAG,"BUILDER: STRING_TEST " + stringBuilder);
                //spannableStrings.add(stringBuilder);
                lines = 1;
                stringBuilder = new SpannableStringBuilder();
            }
            start_position = matcher.end();
        }
//
        return spannableStrings;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_study_poem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        float currentSize = 0;
        switch (item.getItemId()) {
            case R.id.action_settings:
                break;
            case R.id.action_text_bigger:
                /**
                 * TODO поправить условия
                 */
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                int current_scroll = ((ListView) findViewById(R.id.listView)).getScrollY();
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                stringBuilders.clear();
                //Log.d(MainActivity.TAG,);
                if (isPrevDefault) {
                    stringBuilders.addAll(getArray(getSpannableString(sqlWorker.getStringsFromDB(bundle.getString("regex")).getStrings().get(0))));
                } else {
                    tempString = clipboardManager.getPrimaryClip().getItemAt(0).coerceToText(getBaseContext());
                    stringBuilders.addAll(getArray(getSpannableString(tempString.toString())));
                }
                ((ListView) findViewById(R.id.listView)).setScrollY(current_scroll);
                arrayAdapter.notifyDataSetChanged();
                map.clear();

                break;
            /**
             * TODO доработать логику автоматического скрывания объектов
             */
            case R.id.fab_text_hide:
                if (stringBuilders.size() != 0) {
                    for (int i = 0; i < stringBuilders.size(); i++) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(stringBuilders.get(i));
                        Matcher matcher = Pattern.compile("[A-zA-Z0-9а-яА-ЯёЁ]+").matcher(spannableStringBuilder);
                        int count = 0;
                        while (matcher.find()) count++;
                        Log.d(MainActivity.TAG,"TEST_COUNT " + count + " string: " + i);
                        matcher.reset(spannableStringBuilder);
                        if (count != 0) {
                            int test = random.nextInt(count);
                            if (map.get(i) != null && map.get(i).size() != count) {
                                while (map.get(i).contains(test)) {
                                    Log.d(MainActivity.TAG,"TEST: " + test);
                                    test = random.nextInt(count);
                                }
                                map.get(i).add(test);
                            } else if (map.get(i) == null) {
                                map.put(i, new ArrayList<Integer>());
                                map.get(i).add(test);
                            }

                            Log.d(MainActivity.TAG,"StartRandom: " + test + " string: " + i + " MAP: " + map.get(i).toString());

                            int j = 0;
                            while (matcher.find()) {
                                Log.d(MainActivity.TAG,"j: " + j);

                                if (j == test) {
                               /* if(((Spannable)stringBuilders.get(i)).getSpans(0,stringBuilders.get(i).length(),BackgroundColorSpan.class).length != 0)
                                stringBuilders.get(i).removeSpan( ((Spannable)stringBuilders.get(i)).getSpans(0,stringBuilders.get(i).length(),BackgroundColorSpan.class)[test]);*/
                                    BackgroundColorSpan[] colorSpen = ((Spannable) stringBuilders.get(i)).getSpans(matcher.start(), matcher.end(), BackgroundColorSpan.class);
                                    Log.d(MainActivity.TAG,"SPANS_HIDe: " + colorSpen.length + " word: " + matcher.group());
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
                    if (map.get(i) != null) {
                        Matcher matcher = Pattern.compile("[A-zA-Z0-9а-яА-ЯёЁ]+").matcher(stringBuilders.get(i));
                        int j = 0;
                        Log.d(MainActivity.TAG,"j_remove: " + j + " map: " + map.get(i) + " key: " + i);
                        Log.d(MainActivity.TAG,"j_remove: " + j + " map: " + map.get(i).toString() + " key: " + i);
                        if (map.get(i).size() != 0) {
                            while (matcher.find()) {
                                if (j == map.get(i).get(map.get(i).size() - 1)) {
                                    BackgroundColorSpan[] colorSpen = ((Spannable) stringBuilders.get(i)).getSpans(matcher.start(), matcher.end(), BackgroundColorSpan.class);
                                    Log.d(MainActivity.TAG,"SPANS_SHOW: " + colorSpen.length + " word: " + matcher.group());
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
                Log.d(MainActivity.TAG,"STRANGE_CLICK");
                break;

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(MainActivity.TAG,"SCROLL_STATE: " + scrollState);

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > first_visible) {
            if (fab.isShown()) {
                fab.hide();
                fab_random_hide.hide();
                fab_random_show.hide();
            }
            Log.d(MainActivity.TAG,"on_Scroll: " + view.toString() + " first: " + firstVisibleItem);
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
