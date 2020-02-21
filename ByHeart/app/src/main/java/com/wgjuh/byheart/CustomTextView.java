package com.wgjuh.byheart;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by wGJUH on 31.10.2016.
 */

public class CustomTextView extends AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface robotoslab = Typeface.createFromAsset(getContext().getAssets(), "robotoslab_regular.ttf");
            setTypeface(robotoslab);
        }
    }
}
