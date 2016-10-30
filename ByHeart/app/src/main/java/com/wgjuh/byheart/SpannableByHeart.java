package com.wgjuh.byheart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by wGJUH on 30.10.2016.
 */

public class SpannableByHeart extends ClickableSpan {
    private Context context;
    private Point startEnd;

    SpannableByHeart(Context context, int start, int end) {
        this.context = context;
        this.startEnd = new Point();
        this.startEnd.set(start, end);
    }

    @Override
    public void onClick(View widget) {

    }
    @Override
    public void updateDrawState(TextPaint ds) {
        //ds.setColor(Color.BLACK);
        ds.setUnderlineText(false);
    }

    public Point getStartEnd() {
        return startEnd;
    }
}
