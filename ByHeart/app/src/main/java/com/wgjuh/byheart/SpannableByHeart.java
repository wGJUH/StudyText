package com.wgjuh.byheart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ListView;

import static com.wgjuh.byheart.Data.DARK_GREEN;

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
        CustomTextView tv = (CustomTextView) widget;
        ListView listView = (ListView) widget.getParent();

        // TODO add check if tv.getText() instanceof Spanned
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        System.out.println("START: " + start + " END: " + end + " TAG: " + listView.getPositionForView(widget));
        Spannable spannable = new SpannableString(tv.getText());
        BackgroundColorSpan[] colorSpans = spannable.getSpans(start, end, BackgroundColorSpan.class);
        ForegroundColorSpan[] foregroundColorSpans = spannable.getSpans(start, end, ForegroundColorSpan.class);
        if (colorSpans.length != 0 && foregroundColorSpans.length != 0) {
            int color = colorSpans[0].getBackgroundColor();
            spannable.removeSpan(colorSpans[0]);
            spannable.removeSpan(foregroundColorSpans[0]);
            switch (color) {
                case Color.WHITE:
                    spannable.setSpan(new BackgroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case DARK_GREEN:
                  //  spannable.setSpan(new BackgroundColorSpan(context.getResources().getColor(R.color.white)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                  //  spannable.setSpan(new ForegroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }
        } else {
            spannable.setSpan(new BackgroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(DARK_GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

       // tv.setText(spannable, TextView.BufferType.SPANNABLE);
        //Cast spannable to spannableStringBuilder and then sen it with position to update method
        sendUpdateRequest(widget, listView, spannable);
        //Toast.makeText(context,"position: " + listView.getPositionForView(tv),Toast.LENGTH_SHORT).show();
    }

    private void sendUpdateRequest(View widget, ListView listView, Spannable spannable) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(spannable);
        ((StudyPoem)context).updateStringsArray(listView.getPositionForView(widget), spannableStringBuilder);
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
