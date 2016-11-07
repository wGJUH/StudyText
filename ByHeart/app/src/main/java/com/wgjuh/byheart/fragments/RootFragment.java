package com.wgjuh.byheart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgjuh.byheart.myapplication.R;

/**
 * Created by wGJUH on 19.10.2016.
 */

public class RootFragment extends AbstractFragment {
    public PoetsFragment poetsFragment = new PoetsFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_root, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.frame_root, poetsFragment);

        transaction.commit();

        return view;
    }
    public void setShouldUpadated(Boolean should){
        if(getFragmentManager().findFragmentById(R.id.frame_root) instanceof PoemsFragment){

        }
    }

    @Override
    public void deleteItems() {

    }

    @Override
    public void favoriteItems() {

    }

    @Override
    public int[] getSelected() {
        return new int[0];
    }

    @Override
    public void toggleSelectAll() {

    }

    @Override
    public void setMultiSelection(boolean multiSelection, int position) {
        AbstractFragment abstractFragment  = (AbstractFragment) getFragmentManager().findFragmentById(R.id.frame_root);
        if(abstractFragment instanceof PoemsFragment)
            ((PoemsFragment)abstractFragment).setMultiSelection(false,0);

    }
}
