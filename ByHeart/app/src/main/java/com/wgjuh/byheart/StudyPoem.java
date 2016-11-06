package com.wgjuh.byheart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;

import com.github.clans.fab.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionMenu;
import com.wgjuh.byheart.adapters.TextRecyclerView;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudyPoem extends AppCompatActivity implements Data, View.OnClickListener, AbsListView.OnScrollListener {
    private Bundle bundle;
    private SqlWorker sqlWorker;
    private ArrayList<SpannableStringBuilder> spannableStringBuilders;
    public Map<Integer, ArrayList<Integer>> hided = new HashMap<>();
    public ArrayList<ArrayList<Point>> spannableBorders;
    private RecyclerView recyclerView;
    private TextRecyclerView textRecyclerView;
    private int first_visible;
    public FloatingActionMenu floatingActionMenu;
    public String text;
    private com.github.clans.fab.FloatingActionButton fab_hide, fab_show, fab_clear;
    private int counter = 0;
    private int wordsCount = 0;
    private int stringCount = 0;
    private int PROCENT = 5;
    Handler handler;
    ProgressDialog pbCount;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_poem);
        handler = new Handler();
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        sqlWorker = new SqlWorker(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitleFromDB(getTextId()));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fab_hide = (FloatingActionButton) findViewById(R.id.fab_hide);
        fab_show = (FloatingActionButton) findViewById(R.id.fab_show);
        fab_clear = (FloatingActionButton) findViewById(R.id.clear);
        fab_hide.setOnClickListener(this);
        fab_show.setOnClickListener(this);
        fab_clear.setOnClickListener(this);
        spannableStringBuilders = new ArrayList<>();
        spannableBorders = new ArrayList<>();
        text = getTextFromDB(getTextId());
        pbCount = new ProgressDialog(this);
        pbCount.setMessage(getResources().getString(R.string.executing_task));
        pbCount.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pbCount.setProgress(0);
        stringCount = getStringCount(text);
        pbCount.setMax(stringCount);
        if (stringCount > 200)
            pbCount.show();

        listView = (ListView) findViewById(R.id.text_recycler_view);
        textRecyclerView = new TextRecyclerView(this, spannableStringBuilders);
        listView.setOnScrollListener(this);
        // TODO: 03.11.2016 посмотреть: лучше назначать адаптер или делать notifyDataSetChanged
        new Thread(new Runnable() {
            @Override
            public void run() {
                getArray(getSpannableString(text));
                System.out.println("FINISHED");
                pbCount.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(textRecyclerView);
                    }
                });
            }
        }).start();


    }

    private int getTextId() {
        Intent intent = getIntent();
        if (intent != null) {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                return bundle.getInt(KEY_ID, ERROR);
            } else return ERROR;
        } else return ERROR;
    }

    private String getTextFromDB(int id) {
        return sqlWorker.getTextFromDB(id);
    }

    private String getTitleFromDB(int id) {
        return sqlWorker.getTitleFromDB(id);
    }

    private SpannableString getSpannableString(String s) {
        SpannableString text = new SpannableString(s);
        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(s);
        while (matcher.find()) {
            wordsCount++;
            text.setSpan(new SpannableByHeart(this, matcher.start(), matcher.end()), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    private int getStringCount(String s) {
        Matcher matcher = Pattern.compile(PATTERN_STRING).matcher(s);
        int count = 0;
        while (matcher.find())
            count++;
        System.out.println("count: " + count);
        return count;
    }

    // TODO: 03.11.2016 сделать потом обращение к ui thread через handler 
    private void getArray(final SpannableString spannableString) {
        long start = System.currentTimeMillis();
        ArrayList<SpannableStringBuilder> rowArray = new ArrayList<SpannableStringBuilder>();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        Matcher matcher = Pattern.compile(PATTERN_STRING).matcher(spannableString);
        System.out.println("spans: " + Arrays.toString(spannableString.getSpans(spannableString.getSpanStart(spannableString), spannableString.getSpanEnd(spannableString), ClickableSpan.class)));
        int startPosition = 0;
        while (matcher.find()) {
            /*try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            if (matcher.start() - startPosition >= 1) {
                spannableStringBuilder.append(spannableString.subSequence(startPosition, matcher.end() - 1));
                final SpannableStringBuilder finalSpannableStringBuilder = spannableStringBuilder;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        spannableStringBuilders.add(finalSpannableStringBuilder);
                        makeBorders(finalSpannableStringBuilder);

                    }
                });
                spannableStringBuilder = new SpannableStringBuilder();
            }
            handler.post(updateProgress);
            startPosition = matcher.end();
        }
        System.out.println("finish: " + (System.currentTimeMillis() - start));

    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            System.out.println("progress: " + spannableStringBuilders.size());
            pbCount.setProgress(spannableStringBuilders.size());
        }
    };

    private void makeBorders(SpannableStringBuilder spannableStringBuilder) {
        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(spannableStringBuilder);
        spannableBorders.add(new ArrayList<Point>());
        int start = 0;
        while (matcher.find()) {
            Point temp = new Point(matcher.start(), matcher.end());
            System.out.println("temp Point: " + temp + "start: " + matcher.start());
            spannableBorders.get(spannableBorders.size() - 1).add(temp);
            start = matcher.end() + 1;
        }
        System.out.println("Borders: " + spannableBorders.size() + " lenght: " + spannableBorders.get(spannableBorders.size() - 1).size());
    }

    public void updateStringsArray(int position, SpannableStringBuilder spannable) {
        spannableStringBuilders.remove(position);
        spannableStringBuilders.add(position, spannable);
        textRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_show:
                //showWords();
                showWordsByProc();
                break;
            case R.id.fab_hide:/*
                generateHide();
                hideWords();*/
                generateHideInProc();
                hideWordsByProc();
                break;
            case R.id.clear:
                clearAllHiden();
                break;
            default:
                break;
        }
    }

    private void  clearAllHiden(){
        for (int i = 0; i < spannableStringBuilders.size(); i++) {
            spannableStringBuilders.get(i).clearSpans();
        }
        hided.clear();
        textRecyclerView.notifyDataSetChanged();
    }

    private void showWords() {
        System.out.println("Strings sizes: " + spannableStringBuilders.size() +
                " borders: " + spannableBorders.size() + " row " + hided.size());
        for (int i = 0; i < hided.size(); i++) {
            if (hided.get(i).size() != 0) {
                int lastHiden = hided.get(i).get(hided.get(i).size() - 1);
                Point point = spannableBorders.get(i).get(lastHiden);
                BackgroundColorSpan[] backgroundColorSpen = (spannableStringBuilders.get(i)).getSpans(point.x, point.y, BackgroundColorSpan.class);
                (spannableStringBuilders.get(i)).removeSpan(backgroundColorSpen[0]);
                ForegroundColorSpan[] foregroundColorSpen = (spannableStringBuilders.get(i)).getSpans(point.x, point.y, ForegroundColorSpan.class);
                (spannableStringBuilders.get(i)).removeSpan(foregroundColorSpen[0]);
                hided.get(i).remove(hided.get(i).size() - 1);
                hided.get(i).trimToSize();
            }
        }
        textRecyclerView.notifyDataSetChanged();
    }

    private void hideWords() {
        System.out.println("Strings sizes: " + spannableStringBuilders.size() +
                " borders: " + spannableBorders.size() + " row " + hided.size());
        for (int i = 0; i < hided.size(); i++) {
            if (spannableBorders.get(i).size() != 0) {
                int hidenSize = hided.get(i).size() - 1;
                int hiden = hided.get(i).get(hidenSize);
                ArrayList<Point> points = spannableBorders.get(i);
                Point point = points.get(hiden);
                // System.out.println("point: " + point + " array: " +spannableBorders.get(i) + " string lenght: " + spannableStringBuilders.get(i).length());
                (spannableStringBuilders.get(i)).setSpan(new BackgroundColorSpan(DARK_GREEN), point.x, point.y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                (spannableStringBuilders.get(i)).setSpan(new ForegroundColorSpan(DARK_GREEN), point.x, point.y, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textRecyclerView.notifyDataSetChanged();
    }

    private void generateHide() {
        Random random = new Random();
        int rows = spannableStringBuilders.size();
        ArrayList<Integer> words = new ArrayList<>(spannableBorders.size());
        System.out.println("spannableBorders: " + spannableBorders.size());
        for (int i = 0; i < rows; i++) {
            int temp = spannableBorders.get(i).size();
            if (hided.get(i) == null) {
                hided.put(i, new ArrayList<Integer>());
            }
            if (temp != 0) {
                int willBeHided = -1;
                if (hided.get(i).size() < spannableBorders.get(i).size()) {
                    do {
                        willBeHided = random.nextInt(temp);
                        System.out.println("willBeHided : " + willBeHided
                                + " hided.get(i): " + hided.get(i)
                                + " size: " + hided.get(i).size() + " borders: " + spannableBorders.get(i).size());
                    }
                    while (hided.get(i).contains(willBeHided));
                    hided.get(i).add(willBeHided);
                }
            }
        }
        System.out.println("Words: " + hided);
    }
    private void showWordsByProc(){
        int emptyRows = 0;
        boolean emptys = true;
        int showNow = 0;
        int procShown = 0;
        int rows = spannableStringBuilders.size();
        int hidedCount = 0;
        int currentlyHiden = 0;
        for(int i = 0; i < hided.size();i++)
                hidedCount += hided.get(i).size();
        System.out.println("hidedCount: " + hidedCount);
        procShown = wordsCount / PROCENT;
        for(int i = 0; i < spannableBorders.size();i++ )
            if(spannableBorders.get(i).size() == 0) emptyRows++;
        if(procShown < rows){
            procShown = rows - emptyRows;
        }
        for(int i = 0; i < hided.size();i++){
            currentlyHiden += hided.get(i).size();
        }
        if(currentlyHiden < rows){
           procShown = currentlyHiden;
        }
        System.out.println("procShown: " + procShown);

        do {

            for (int i = 0; i < spannableStringBuilders.size() && procShown != showNow; i++) {
                if (spannableBorders.get(i).size() == 0 && emptys){
                    continue;
            }
                Spannable spannable = (Spannable) spannableStringBuilders.get(i);
                BackgroundColorSpan[] backgroundColorSpen = (spannableStringBuilders.get(i)).getSpans(0, spannable.length(), BackgroundColorSpan.class);
                ForegroundColorSpan[] foregroundColorSpen = (spannableStringBuilders.get(i)).getSpans(0, spannable.length(), ForegroundColorSpan.class);
                if (backgroundColorSpen.length != 0) {
                    (spannableStringBuilders.get(i)).removeSpan(backgroundColorSpen[backgroundColorSpen.length - 1]);
                    (spannableStringBuilders.get(i)).removeSpan(foregroundColorSpen[foregroundColorSpen.length - 1]);
                    hided.get(i).remove(hided.get(i).size()-1);
                    showNow++;
                }
            }
            emptys = false;
            System.out.println("procShown: " + procShown + " showNow: " + showNow);
        }while (procShown != showNow);
        // TODO: 06.11.2016 пока что оставлю очищение при откате закрашивания
        textRecyclerView.notifyDataSetChanged();
    }
    private void generateHideInProc() {
        Random random = new Random();
        int procHiden = 0;
        int hidenNow = 0;
        int rows = spannableBorders.size();
        //нужно для учета пустых строк на первом круге
        boolean emptys = true;
        //Сколько слов надо закрасить
        procHiden = wordsCount / PROCENT;
        int currentlyHiden = 0;
        int emptyRows = 0;
        for(int i = 0; i < spannableBorders.size();i++ )
            if(spannableBorders.get(i).size() == 0) emptyRows++;
        if(procHiden < rows){
            procHiden = rows - emptyRows;
        }
        for(int i = 0; i < hided.size();i++){
            currentlyHiden += hided.get(i).size();
        }
        if (procHiden > (wordsCount - currentlyHiden)){


        procHiden = wordsCount - currentlyHiden;
        }

        do {
            //увеличиваем счетчик на количество пустых строк
            /*if(emptys){
                procHiden += emptyRows;
            }*/
            //проходим по строкам
            for (int i = 0; i < rows && hidenNow != procHiden; i++) {
                if (hided.get(i) == null) {
                    hided.put(i, new ArrayList<Integer>());
                }
                //если размер строки с возможными границами больше
                if (spannableBorders.get(i).size() > hided.get(i).size() && spannableBorders.get(i).size() != 0) {
                    int willBeHiden = -1;
                    do {
                        willBeHiden = random.nextInt(spannableBorders.get(i).size());
                    } while (hided.get(i).contains(willBeHiden));
                    hided.get(i).add(willBeHiden);
                    hidenNow++;
                    //на первом круге увеличиваем счетчик при пустых строках
                }else if (spannableBorders.get(i).size() == 0 && emptys) /*hidenNow++*/continue;
            }
            System.out.println("generated: " + hidenNow + " shouldBeGenerated: " + procHiden + " all Words: " + wordsCount );
            emptys = false;
        } while (hidenNow != procHiden);
        currentlyHiden=0;
        for(int i = 0; i < hided.size();i++){
            currentlyHiden += hided.get(i).size();
        }
        System.out.println("wordsCount: " + wordsCount + " currentlyHiden: " + currentlyHiden );
    }

    private void hideWordsByProc() {

        int start = -1, end = -1;
        Point point;
        for (int i = 0; i < hided.size(); i++) {
            //получаем строку
            Spannable spannable = (Spannable) spannableStringBuilders.get(i);
            //получаем границы слов по порядку
            for (int j = 0; j < hided.get(i).size(); j++) {
                point = spannableBorders.get(i).get(hided.get(i).get(j));
                start = point.x;
                end = point.y;
                BackgroundColorSpan[] backgroundColorSpans = spannable.getSpans(start, end, BackgroundColorSpan.class);
                ForegroundColorSpan[] foregroundColorSpans = spannable.getSpans(start, end, ForegroundColorSpan.class);
                if (backgroundColorSpans.length != 0) {
                    int color = backgroundColorSpans[0].getBackgroundColor();
                    switch (color) {
                        case Color.WHITE:
                            spannable.setSpan(new BackgroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannable.setSpan(new ForegroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            break;
                        case DARK_GREEN:
                            break;
                        default:
                            break;
                    }
                }else{
                    spannable.setSpan(new BackgroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }

        textRecyclerView.notifyDataSetChanged();

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_study_poem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!floatingActionMenu.isMenuHidden()) {
            //System.out.println("isShown");
            floatingActionMenu.close(true);
        }
    }
}
