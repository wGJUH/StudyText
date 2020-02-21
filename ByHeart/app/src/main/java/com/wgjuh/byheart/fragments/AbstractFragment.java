package com.wgjuh.byheart.fragments;

import androidx.fragment.app.Fragment;

/**
 * Created by wGJUH on 02.11.2016.
 */

public abstract class AbstractFragment extends Fragment {
    public abstract void deleteItems();
    public abstract void favoriteItems();
    public abstract int[] getSelected();
    public void updateValues(int position){};
    public void updateValues(int position, boolean favorite){}
    public abstract void toggleSelectAll();
    public abstract void setMultiSelection(boolean multiSelection,int position);
}
