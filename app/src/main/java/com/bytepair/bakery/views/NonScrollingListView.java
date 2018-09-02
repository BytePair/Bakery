package com.bytepair.bakery.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Found on stack overflow by Dedaniya HirenKumar
 * https://stackoverflow.com/questions/18813296/non-scrollable-listview-inside-scrollview
 */
public class NonScrollingListView extends ListView {
    public NonScrollingListView(Context context) {
        super(context);
    }

    public NonScrollingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollingListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
