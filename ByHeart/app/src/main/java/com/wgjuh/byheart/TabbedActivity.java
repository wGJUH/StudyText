package com.wgjuh.byheart;

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
import com.wgjuh.byheart.myapplication.R;

public class TabbedActivity extends AppCompatActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private SqlWorker sqlWorker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabbed_activity);
        sqlWorker = new SqlWorker(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
       setViewpagerTitle();
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        setupPortraitOrientation();
        else setupLandscapeOrientation();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupLandscapeOrientation(){
        System.out.println("setupLandscapeOrientation");
        FrameLayout poetsFrame = (FrameLayout)findViewById(R.id.frame_poets);
        FrameLayout poemsFrame = (FrameLayout)findViewById(R.id.frame_poems);
        FragmentTransaction fragmentTransaction = (FragmentTransaction)getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_poets,new PoetsFragment());
        fragmentTransaction.replace(R.id.frame_poems,new PoemsFragment());
        fragmentTransaction.commit();
    }

    private void setupPortraitOrientation() {
        System.out.println("setupPortraitOrientation");
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTabsTitles();
    }

    private void setViewpagerTitle(){
        TextView viewTitle = (TextView)findViewById(R.id.toolbar_title);
        viewTitle.setText(getString(R.string.app_name));
        setTypeFace(viewTitle);
    }
    private void setTabsTitles(){
        TextView libTab = (TextView)LayoutInflater.from(this).inflate(R.layout.custom_tab_title,null);
        libTab.setText(getString(R.string.title_lib));
        tabLayout.getTabAt(0).setCustomView(libTab);
        TextView favoriteTab = (TextView)LayoutInflater.from(this).inflate(R.layout.custom_tab_title,null);
        favoriteTab.setText(getString(R.string.title_favorite));
        tabLayout.getTabAt(1).setCustomView(favoriteTab);
        setTypeFace(libTab);
        setTypeFace(favoriteTab);
    }
    private void setTypeFace(TextView textView){
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }
    private  void setupViewPager(ViewPager mViewPager){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new PoetsFragment(),getString(R.string.title_lib));
        adapter.addFragment(new FavoriteFragment(),getString(R.string.title_favorite));
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
}
