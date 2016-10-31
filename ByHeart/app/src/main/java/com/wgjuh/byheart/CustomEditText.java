package com.wgjuh.byheart;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by wGJUH on 31.10.2016.
 */

public class CustomEditText extends EditText {
    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    private void init() {
        if (!isInEditMode()) {
            Typeface robotoslab = Typeface.createFromAsset(getContext().getAssets(), "robotoslab_regular.ttf");
            setTypeface(robotoslab);
        }
    }
}
