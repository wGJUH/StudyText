package com.wgjuh.byheart.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    //добавляем фрагменты в список
    public void addFragment(Fragment fragment, String title){
        if(fragment!= null && title != null && !title.equals("")){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
