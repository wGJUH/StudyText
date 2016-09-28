package com.studypoem.wgjuh.studypoem;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by tjcf46 on 28.09.2016.
 */
public class AuthorsGrid extends GridLayoutManager {
    private int minItemWidth;
    Context context;
    private int mColumnWidth;
    private boolean mColumnWidthChanged = true;
    public AuthorsGrid(Context context,int minItemWidth) {
        super(context, 1);
        this.minItemWidth = minItemWidth;
        this.context = context;
        setColumnWidth(checkedColumnWidth(context, minItemWidth));
    }



    private int checkedColumnWidth(Context context, int columnWidth)
    {
        if (columnWidth <= 0)
        {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48,
                    context.getResources().getDisplayMetrics());
        }
        return columnWidth;
    }

    public void setColumnWidth(int newColumnWidth)
    {
        if (newColumnWidth > 0 && newColumnWidth != mColumnWidth)
        {
            mColumnWidth = newColumnWidth;
            mColumnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state)
    {
        int width = getWidth();
        int height = getHeight();
        if (mColumnWidthChanged && mColumnWidth > 0 && width > 0 && height > 0)
        {
            int totalSpace;
            if (getOrientation() == VERTICAL)
            {
                totalSpace = width - getPaddingRight() - getPaddingLeft();
            }
            else
            {
                totalSpace = height - getPaddingTop() - getPaddingBottom();
            }
            int spanCount = Math.max(1, totalSpace / mColumnWidth);
            setSpanCount(spanCount);
            mColumnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }



/*    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateSpanCount();
        super.onLayoutChildren(recycler, state);
    }
    private void updateSpanCount(){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        int dpi = metrics.densityDpi;
        int spanCount = getWidth() / minItemWidth;
        if (spanCount < 1) {
            spanCount = 1;
        }
        this.setSpanCount(spanCount);
    }*/
}
