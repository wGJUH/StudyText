package com.studypoem.wgjuh.byheart;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/* Created by WGJUH on 18.09.2016.*/


public class MyTextAdapter extends ArrayAdapter {

    LoremIpsumSpan[] loremIpsumSpen;
    SpannableString spannableString;
    Context context;
    ArrayList<SpannableStringBuilder> strings;
    String s;
    static float setTextSize = 0;
    public MyTextAdapter(StudyPoem studyPoem, int list_view_test, int my_custom_text,  ArrayList<SpannableStringBuilder> array) {
        super(studyPoem,R.layout.my_custom_textview, array);
//        loremIpsumSpen = spannableString.getSpans(0,spannableString.length(),LoremIpsumSpan.class);
        this.strings = array;
        this.context = studyPoem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        LoremIpsumSpan  loremIpsumSpan = (LoremIpsumSpan) getItem(position);
//        System.out.println("Position: " + spannableString.subSequence(spannableString.getSpanStart(loremIpsumSpan),spannableString.getSpanEnd(loremIpsumSpan)));
        LayoutInflater mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) mLayoutInflator.inflate(R.layout.my_custom_textview,null);
       // System.out.println("BUILDER: " + getItem(position).toString());
        textView.setText((SpannableStringBuilder)getItem(position));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        if(setTextSize != 0)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,setTextSize);
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);
        return textView;
    }
    public static void updateSize(Context context, int step, float currentSize){
        LayoutInflater mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) mLayoutInflator.inflate(R.layout.my_custom_textview,null);
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentSize+step);

        setTextSize = currentSize+step;
    }
}
