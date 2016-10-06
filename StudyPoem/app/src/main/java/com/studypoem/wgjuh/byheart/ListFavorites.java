package com.studypoem.wgjuh.byheart;

/**
 * Created by wGJUH on 06.10.2016.
 */

public class ListFavorites extends ListPoems {
    @Override
    public Values getValues() {
        return sqlWorker.getStarred();
    }
}
