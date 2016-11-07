package com.wgjuh.byheart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.clans.fab.FloatingActionButton;

/**
 * Created by wGJUH on 07.11.2016.
 */

public class CustomFloatingButton extends FloatingActionButton {
    public CustomFloatingButton(Context context) {
        super(context);
    }

    public CustomFloatingButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomFloatingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLabelVisibility(int visibility) {

        super.setLabelVisibility(VISIBLE);
    }

    @Override
    public int getLabelVisibility() {
        return VISIBLE;
    }

}
