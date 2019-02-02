package com.modaberi.widget;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by smoda on 3/4/2018.
 */

public class ModaSquareRelativeLayout extends RelativeLayout {

    public ModaSquareRelativeLayout(Context context) {
        super(context);
    }

    public ModaSquareRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ModaSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ModaSquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}