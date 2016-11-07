package com.studypoem.wgjuh.byheart;

/**
 * Created by wGJUH on 06.10.2016.
 */

public class ListFavorites extends ListPoems {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public void runSetters() {
        super.runSetters();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public Values getValues() {
        return sqlWorker.getStarred();
    }
}
