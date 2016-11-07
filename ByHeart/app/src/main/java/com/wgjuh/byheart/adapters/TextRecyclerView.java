package com.wgjuh.byheart.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wgjuh.byheart.Data;
import com.wgjuh.byheart.Values;
import com.wgjuh.byheart.fragments.PoemsFragment;
import com.wgjuh.byheart.myapplication.BuildConfig;
import com.wgjuh.byheart.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wGJUH on 19.10.2016.
 */

public class TextRecyclerView extends ArrayAdapter /*implements View.OnClickListener */{
    Context context;
    ArrayList<SpannableStringBuilder> strings;
    float setTextSize = 0;
    float defaultTextSize ;
    ViewHolder viewHolder;
    public TextRecyclerView(Context context, ArrayList<SpannableStringBuilder> array) {
        super(context, R.layout.list_text_single_element, array);
        this.strings = array;
        this.context = context;
    }

    static class ViewHolder {
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflator.inflate(R.layout.list_text_single_element, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView)convertView;
            viewHolder.textView.setFocusable(false);
            viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.textView.setFocusableInTouchMode(false);
            convertView.setTag(viewHolder);
        }else viewHolder = (ViewHolder)convertView.getTag();
        viewHolder.textView.setText((SpannableStringBuilder)getItem(position));
        if(setTextSize != 0) {
            viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, setTextSize);
        }else defaultTextSize = viewHolder.textView.getTextSize();
        return convertView;
    }
    public void updateSize(Context context, int step, float currentSize) {
        if(currentSize <= defaultTextSize+10 && step > 0 ) {
            setTextSize = currentSize + step;
        } else if(currentSize >= defaultTextSize && step < 0){
            setTextSize = currentSize +step;
        }
        notifyDataSetChanged();
    }
    public TextView getTextView(){
        return viewHolder.textView;
    }
}
