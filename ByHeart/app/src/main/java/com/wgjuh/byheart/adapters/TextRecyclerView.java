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
/*public class TextRecyclerView extends RecyclerView.Adapter<TextRecyclerView.ViewHolder> implements Data {
    private ArrayList<SpannableStringBuilder> spannableStringBuilders;
    private Context context;
    private ViewHolder viewHolder;

    public TextRecyclerView(Context context, ArrayList<SpannableStringBuilder> spannableStringBuilders) {
        this.context = context;
        this.spannableStringBuilders = spannableStringBuilders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View singleElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_text_single_element, parent, false);
        viewHolder = new ViewHolder(singleElement);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ((TextView) holder.mLayout).setText(spannableStringBuilders.get(position));
    }
    @Override
    public int getItemCount() {
        return spannableStringBuilders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public View mLayout;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mLayout = v;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,"CLICK",Toast.LENGTH_SHORT).show();
        }
        @Override
        public boolean onLongClick(View v) {


           // v.setSelected(!v.isSelected());
            return true;
        }
    }
}*/
public class TextRecyclerView extends ArrayAdapter /*implements View.OnClickListener */{
    Context context;
    ArrayList<SpannableStringBuilder> strings;
    static float setTextSize = 0;

    public TextRecyclerView(Context context, ArrayList<SpannableStringBuilder> array) {
        super(context, R.layout.list_text_single_element, array);
        this.strings = array;
        this.context = context;
    }

    /*@Override
    public void onClick(View v) {

    }*/

    static class ViewHolder {
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
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
        return convertView;
    }
    public static void updateSize(Context context, int step, float currentSize) {
        LayoutInflater mLayoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) mLayoutInflator.inflate(R.layout.list_text_single_element, null);
        setTextSize = currentSize + step;
    }
}
