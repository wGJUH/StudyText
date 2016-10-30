package com.wgjuh.byheart;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wgjuh.byheart.adapters.FragmentAdapter;
import com.wgjuh.byheart.fragments.FavoriteFragment;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.fragments.RootFragment;
import com.wgjuh.byheart.myapplication.R;

public class TabbedActivity extends AppCompatActivity implements View.OnClickListener, Data {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SqlWorker sqlWorker;
    private RootFragment rootFragment;
    private FavoriteFragment favoriteFragment;
    public static boolean isShouldBeUpdated = false;
    private FragmentAdapter adapter;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() != 0){
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
        }else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_activity);
        sqlWorker = new SqlWorker(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rootFragment = new RootFragment();
        favoriteFragment = new FavoriteFragment();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setViewpagerTitle();
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            setupPortraitOrientation();
        else setupLandscapeOrientation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(this);
    }

    public static boolean isShouldUpdate() {
        return isShouldBeUpdated;
    }

    // public static void setShouldUpdate(boolean b){isShouldBeUpdated = b;}
    public static void setShouldUpdate(boolean b) {
        isShouldBeUpdated = b;
    }

    private void setupLandscapeOrientation() {
        System.out.println("setupLandscapeOrientation");
        FrameLayout poetsFrame = (FrameLayout) findViewById(R.id.frame_poets);
        FrameLayout poemsFrame = (FrameLayout) findViewById(R.id.frame_poems);
        FragmentTransaction fragmentTransaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_poets, new PoetsFragment());
        fragmentTransaction.replace(R.id.frame_poems, new FavoriteFragment());
        fragmentTransaction.commit();
    }

    private void setupPortraitOrientation() {
        System.out.println("setupPortraitOrientation");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTabsTitles();
    }

    private void setViewpagerTitle() {
        TextView viewTitle = (TextView) findViewById(R.id.toolbar_title);
        viewTitle.setText(getString(R.string.app_name));
        setTypeFace(viewTitle);
    }
    private String getViewPagerTitle(){
        TextView viewTitle = (TextView) findViewById(R.id.toolbar_title);
        return viewTitle.getText().toString();
    }
    private void setTabsTitles() {
        TextView libTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_title, null);
        libTab.setText(getString(R.string.title_lib));
        tabLayout.getTabAt(0).setCustomView(libTab);
        TextView favoriteTab = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_title, null);
        favoriteTab.setText(getString(R.string.title_favorite));
        tabLayout.getTabAt(1).setCustomView(favoriteTab);
        setTypeFace(libTab);
        setTypeFace(favoriteTab);
    }

    private void setTypeFace(TextView textView) {
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }

    private void setupViewPager(ViewPager mViewPager) {
        adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(rootFragment, getString(R.string.title_lib));
        adapter.addFragment(favoriteFragment, getString(R.string.title_favorite));
        mViewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.fab_add:

                System.out.println(adapter.getItem(mViewPager.getCurrentItem()));
                if (adapter.getItem(mViewPager.getCurrentItem()) instanceof RootFragment) {
                    if (rootFragment.getFragmentManager().findFragmentById(R.id.frame_root) instanceof PoetsFragment) {
                        System.out.println("New Author");
                        intent = new Intent(this, NewAuthorActivity.class);
                        startActivityForResult(intent, REQUEST_ADD_NEW_AUTHOR);
                    } else {
                        System.out.println("new Poems");
                        intent = new Intent(this, NewPoemActivity.class);
                        intent.putExtra(KEY_AUTHOR,getViewPagerTitle());
                        startActivityForResult(intent,REQUEST_ADD_NEW_POEM);
                    }
                } else {
                    System.out.println("new Favorite");
                    intent = new Intent(this, NewPoemActivity.class);
                    intent.putExtra(KEY_AUTHOR,getString(R.string.app_name));
                    startActivityForResult(intent,REQUEST_ADD_NEW_FAVORITE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NEW_AUTHOR && resultCode == RESULT_OK) {
            String s = data.getStringExtra(KEY_AUTHOR);
            long resultId = data.getLongExtra(KEY_ID,-777L);
            if(resultId == -777L){
                System.out.println("FAILE");
                return;
            }
            System.out.println("name: " + s);
            //int n = sqlWorker.getRowNumber(s);
            int n = sqlWorker.getRowNumber(resultId);
            ((PoetsFragment) rootFragment.getFragmentManager().findFragmentById(R.id.frame_root)).updateValues(n);
        }else if(requestCode == REQUEST_ADD_NEW_FAVORITE && resultCode == RESULT_OK){
            String s = data.getStringExtra(KEY_TITLE);
            System.out.println("title: " + s);
            int n = sqlWorker.getRowPoemNumber(null,s);
            System.out.println("number: " + n);
            ((FavoriteFragment) adapter.getItem(mViewPager.getCurrentItem())).updateValues(n,true);
        }else if(requestCode == REQUEST_ADD_NEW_POEM && resultCode == RESULT_OK){
            String s = data.getStringExtra(KEY_TITLE);
            System.out.println("title: " + s);
            int n = sqlWorker.getRowPoemNumber(null,s);
            System.out.println("number: " + n);
            ((PoemsFragment) rootFragment.getFragmentManager().findFragmentById(R.id.frame_root)).updateValues(n);        }
    }
}
