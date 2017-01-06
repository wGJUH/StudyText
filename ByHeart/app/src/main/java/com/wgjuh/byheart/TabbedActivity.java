package com.wgjuh.byheart;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wgjuh.byheart.adapters.FragmentAdapter;
import com.wgjuh.byheart.fragments.AbstractFragment;
import com.wgjuh.byheart.fragments.FavoriteFragment;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.fragments.PoetsFragment;
import com.wgjuh.byheart.fragments.RootFragment;
import com.wgjuh.byheart.myapplication.R;

import static android.R.id.message;

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
    private boolean isFabForDelete = false;
    private boolean isFabForFavorite = false;
    private AbstractFragment fragmentMultiSelection;
    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() != 0){
            FavoriteFragment favoriteFragment = ((FavoriteFragment)adapter.getItem(mViewPager.getCurrentItem()));
            if(favoriteFragment.getMultiSelection()){
                favoriteFragment.setMultiSelection(false,0);
            }else  mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
        }else {
            ((AbstractFragment) adapter.getItem(mViewPager.getCurrentItem())).setMultiSelection(false, 0);
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        //if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))
        requestMultiplePermissions();

        super.onCreate(savedInstanceState);

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
    protected void onResume() {
        //
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Data.PERMISSION_REQUEST_CODE && grantResults.length == 2){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) ){
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

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                System.out.println("child: " + adapter.getItem(position));
                FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
                if(position == 0){
                    if (rootFragment.getFragmentManager().findFragmentById(R.id.frame_root) instanceof PoetsFragment){
                        PoetsFragment poetsFragment = ((PoetsFragment)rootFragment.getFragmentManager().findFragmentById(R.id.frame_root));
                        poetsFragment.updateToolbar(poetsFragment.getMultiSelection());
                    }else{
                        PoemsFragment poemsFragment = ((PoemsFragment)rootFragment.getFragmentManager().findFragmentById(R.id.frame_root));
                        poemsFragment.updateToolbar(poemsFragment.getMultiSelection());
                    }
                    if(isFabForDelete)
                        floatingActionButton.setImageResource(R.drawable.delete_hover);
                }else{
                    FavoriteFragment favoriteFragment = ((FavoriteFragment)adapter.getItem(position));
                    favoriteFragment.updateToolbar(favoriteFragment.getMultiSelection());
                    if(isFabForFavorite)
                        floatingActionButton.setImageResource(R.drawable.favoritewhite);
                }
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
       // setTypeFace(viewTitle);
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
        /*setTypeFace(libTab);
        setTypeFace(favoriteTab);*/
    }

    /*private void setTypeFace(TextView textView) {
        Typeface robotoslab = Typeface.createFromAsset(getAssets(), "robotoslab_regular.ttf");
        textView.setTypeface(robotoslab);
    }*/
    public void updateFavorites(){
        ((FavoriteFragment)adapter.getItem(1)).updateValues(0,false);
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

    public void replaceMenu(){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_selection:
                //Toast.makeText(this,"SELECT ALL",Toast.LENGTH_SHORT).show();
                fragmentMultiSelection.toggleSelectAll();
                break;
            case R.id.action_favorites:
               // Toast.makeText(this,"FAVORITE ALL",Toast.LENGTH_SHORT).show();
                fragmentMultiSelection.favoriteItems();
                break;
            case R.id.action_delete:
                //Toast.makeText(this,"DELETE",Toast.LENGTH_SHORT).show();
                fragmentMultiSelection.deleteItems();
                break;
            case R.id.action_settings:
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    public void updateFabFunction(AbstractFragment fragment,boolean isFabFor){
        System.out.println("Update function");
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add);
        if(!(fragment instanceof FavoriteFragment)){
            isFabForFavorite = isFabFor;
        if(this.isFabForDelete != isFabFor) {
            this.isFabForDelete = isFabFor;
            floatingActionButton.hide(true);
            if (this.isFabForDelete) {
                System.out.println("FAB for delete");
                floatingActionButton.setImageResource(R.drawable.delete_hover);

            } else {
                System.out.println("FAB for create");
                floatingActionButton.setImageResource(R.drawable.create);
            }
            floatingActionButton.show(true);
        }
    }else if(this.isFabForFavorite != isFabFor) {
            isFabForDelete = isFabFor;
            System.out.println("FabForFavorite: " + isFabFor);
            this.isFabForFavorite = isFabFor;
            floatingActionButton.hide(true);
            if (this.isFabForFavorite) {
                System.out.println("FAB for favorites");
                floatingActionButton.setImageResource(R.drawable.favoritewhite);
            } else {
                System.out.println("FAB for create");
                floatingActionButton.setImageResource(R.drawable.create);
            }
            floatingActionButton.show(true);
        }
        fragmentMultiSelection = fragment;
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        if(!isFabForDelete && !isFabForFavorite)
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
        else if(isFabForDelete && mViewPager.getCurrentItem() == 0){
           // Toast.makeText(this,"DELETE",Toast.LENGTH_SHORT).show();
            fragmentMultiSelection.deleteItems();
        }else if (isFabForFavorite && mViewPager.getCurrentItem() == 1){
            //Toast.makeText(this,"FAVORITE ALL",Toast.LENGTH_SHORT).show();
            fragmentMultiSelection.favoriteItems();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_NEW_AUTHOR && resultCode == RESULT_OK) {
            String s = data.getStringExtra(KEY_AUTHOR);
            long resultId = data.getLongExtra(KEY_ID,-777L);
            System.out.println("name after save: " + s + " id: " + resultId);
            if(resultId == -777L){
                System.out.println("FAIL");
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
            int n = sqlWorker.getRowPoemNumber(data.getStringExtra(KEY_AUTHOR),s);
            System.out.println("number: " + n);
            ((PoemsFragment) rootFragment.getFragmentManager().findFragmentById(R.id.frame_root)).updateValues(n);        }
    }
}
