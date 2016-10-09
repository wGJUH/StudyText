package com.studypoem.wgjuh.byheart;

import java.util.ArrayList;

/**
 * Created by tjcf46 on 28.09.2016.
 */
final class Values{
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    private ArrayList<String> portraitIds;
    Values(ArrayList<String> strings, ArrayList<Integer> ids){
        this.strings = strings;
        this.ids = ids;
    }
    Values(ArrayList<String> strings, ArrayList<Integer> ids, ArrayList<String> portraitIds){
        this.strings = strings;
        this.ids = ids;
        this.portraitIds = portraitIds;
    }
    public ArrayList<String> getStrings(){
        return strings;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public ArrayList<String> getPortraitIds() {
        return portraitIds;
    }

    public void setStrings(ArrayList<String> strings){
        ArrayList<String> temp = (ArrayList<String>) strings.clone();
        this.strings.clear();
        this.strings.addAll(temp);
    }
    public void setIds(ArrayList<Integer> ids){
        ArrayList<Integer> temp = (ArrayList<Integer>) ids.clone();
        this.ids.clear();
        this.ids.addAll(temp);
    }
    public void setPortraitIds (ArrayList<String> portraitIds){
        ArrayList<String> temp = (ArrayList<String>) portraitIds.clone();
        this.portraitIds.clear();
        this.portraitIds.addAll(temp);
    }
    public void setAll(Values values){
       setIds(values.getIds());
        setPortraitIds(values.getPortraitIds());
        setStrings(values.getStrings());
    }
}
