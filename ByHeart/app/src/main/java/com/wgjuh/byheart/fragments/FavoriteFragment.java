package com.wgjuh.byheart.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wgjuh.byheart.myapplication.R;

/**
 * Created by wGJUH on 18.10.2016.
 */

public class FavoriteFragment extends Fragment {
    public FavoriteFragment() {
        //super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_poems,container,false);
    }
}
