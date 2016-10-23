package com.wgjuh.byheart;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by tjcf46 on 28.09.2016.
 */
public final class Values {
    private ArrayList<String> strings;
    private ArrayList<Integer> ids;
    private ArrayList<String> portraitIds;
    private ArrayList<Boolean> starrs;



    Values(ArrayList<String> strings, ArrayList<String> portraitIds){
        this.strings = strings;
        this.ids = ids;
        this.portraitIds = portraitIds;
    }
    Values(ArrayList<String> strings, ArrayList<Boolean> starrs, ArrayList<Integer> ids){
        this.strings = strings;
        this.starrs = starrs;
        this.ids = ids;
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

    public ArrayList<Boolean> getStarrs() {
        return starrs;
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

    public void setStarrs(ArrayList<Boolean> starrs) {
        ArrayList<Boolean> temp = (ArrayList<Boolean>)starrs.clone();
        this.starrs.clear();
        this.starrs.addAll(temp);
    }

    public void setAll(Values values){
        setIds(values.getIds());
        setPortraitIds(values.getPortraitIds());
        setStrings(values.getStrings());
    }

}
