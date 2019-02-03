package com.modaberi.widget.buttons;

/**
 * Created by smoda on 6/22/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.modaberi.widget.KeyValue;
import com.modaberi.widget.ModaRippleView;
import com.modaberi.widget.R;
import com.modaberi.widget.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class ModaButtonBGPrimary extends LinearLayout {

    private Button mButton;
    private AVLoadingIndicatorView mAvlLoading;

    public ModaButtonBGPrimary(Context context) {
        super(context);
    }

    public ModaButtonBGPrimary(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData(context, attrs);
    }

    public ModaButtonBGPrimary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ModaButton);
        String hintStr = typedArray.getString(R.styleable.ModaButton_button_text);

        View rootView = LayoutInflater.from(context).inflate(R.layout.moda_button_bg_primary, this);

//        ViewGroup mContainerButton = rootView.findViewById(R.id.container_button);
        mButton = rootView.findViewById(R.id.button);
        mAvlLoading=rootView.findViewById(R.id.avl_loading);

        mButton.setText(hintStr != null && !hintStr.equals("")?hintStr:context.getString(R.string.label_done));

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            return performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void onLoading(){
        mButton.setVisibility(GONE);
        mAvlLoading.setVisibility(VISIBLE);
    }

    public void onResponse(){
        mButton.setVisibility(VISIBLE);
        mAvlLoading.setVisibility(GONE);
    }

}