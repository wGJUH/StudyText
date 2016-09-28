package com.studypoem.wgjuh.studypoem;

import android.content.res.TypedArray;

import java.util.ArrayList;

/**
 * Created by tjcf46 on 28.09.2016.
 */
final class Values{
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    Values(ArrayList<String> strings, ArrayList<Integer> ids){
        this.strings = strings;
        this.ids = ids;
    }
    public ArrayList<String> getStrings(){
        return strings;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }
}
