package com.wgjuh.byheart;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wgjuh.byheart.myapplication.R;

/**
 * Created by wGJUH on 06.01.2017.
 */

public class SpinnerOptions extends Spinner{
    private Spinner spinner;
    private ArrayAdapter<CharSequence> options;
    public SpinnerOptions(Spinner spinner, Context context){
        super(context);
        this.spinner = spinner;
                 }
    public SpinnerOptions createAdapter(int dataArray){
        options = ArrayAdapter.createFromResource(this.getContext(), dataArray, android.R.layout.simple_spinner_item);
        return this;
    }
    public SpinnerOptions setDropDownView(int id){
        options.setDropDownViewResource(id);
        return this;
    }
    public SpinnerOptions setAdapter(){
        spinner.setAdapter(options);
        return this;
    }
}
