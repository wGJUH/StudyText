package com.studypoem.wgjuh.byheart;

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
    public void setStrings(ArrayList<String> strings){
        ArrayList<String> temp = (ArrayList<String>) strings.clone();
        this.strings.clear();
        this.strings.addAll(temp);
    }
}
