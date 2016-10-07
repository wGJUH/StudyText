package com.studypoem.wgjuh.byheart;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.studypoem.wgjuh.byheart.Data.PATTERN_WORD;


public class LoremIpsumSpan extends ClickableSpan {
    StudyPoem studyPoem;
    LoremIpsumSpan(StudyPoem context) {
        studyPoem = context;
        //studyPoem.showSpannebleSize();
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLACK);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        // TODO add check if widget instanceof TextView
        TextView tv = (TextView) widget;
        ListView listView = (ListView) widget.getParent();

        // TODO add check if tv.getText() instanceof Spanned
        Spanned s = (Spanned) tv.getText();
        int start = s.getSpanStart(this);
        int end = s.getSpanEnd(this);
        System.out.println("START: " + start + " END: " + end + " TAG: " + listView.getPositionForView(widget));
        Spannable spannable = new SpannableString(tv.getText());
        BackgroundColorSpan[] colorSpans = spannable.getSpans(start, end, BackgroundColorSpan.class);
        if (colorSpans.length != 0) {
            System.out.println("getSpans !!!!!!!!!" + colorSpans[0].getBackgroundColor() + " length: " + colorSpans.length);
            int color = colorSpans[0].getBackgroundColor();
            spannable.removeSpan(colorSpans[0]);
            switch (color) {
                case Color.WHITE:
                    spannable.setSpan(new BackgroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case Color.BLACK:
                    spannable.setSpan(new BackgroundColorSpan(Color.WHITE), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
            }
        } else
            spannable.setSpan(new BackgroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable, TextView.BufferType.SPANNABLE);
/**
 *         TODO Найти более хороший и красивый способ изменить текущий массив
 */

        Matcher matcher = Pattern.compile(PATTERN_WORD).matcher(s);
        int i =0;
        while (matcher.find()) {
            if(matcher.start() == start) break;
            i++;

        }
        studyPoem.setSpannableString(listView.getPositionForView(widget), new SpannableStringBuilder(spannable));
        studyPoem.setMap(listView.getPositionForView(widget),i);
    }
}
