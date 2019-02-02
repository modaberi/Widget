package com.modaberi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ModaListView extends RelativeLayout {

    private View rootView;
    @ColorInt
    private int errorColor;
    @ColorInt
    private int errorSubColor;
    private String errorMessage;
    private String errorSubMessage;
    @ColorInt
    private int loadingColor;
    private String loadingMessage;

    private Button mBtnErrorAction;
    private TextView mTvErrorMsg;
    private TextView mTvErrorSubMsg;
    private TextView mTvLoadingMsg;
    private RecyclerView mRecycler;
    private ViewGroup mContainerLoading;
    private ViewGroup mContainerError;

    public RecyclerView getRecycler() {
        return mRecycler;
    }

    public interface OnListActionListener {
        void onError();
    }

    private OnListActionListener mListener;

    public void setListener(OnListActionListener listener) {
        this.mListener = listener;
    }

    public ModaListView(Context context) {
        super(context);
    }

    public ModaListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData(context, attrs);
    }

    public ModaListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomListView);
        errorMessage = typedArray.getString(R.styleable.CustomListView_tv_error_message);
        errorColor = typedArray.getColor(R.styleable.CustomListView_clr_error_message, ContextCompat.getColor(context, R.color.text_color));

        errorSubMessage = typedArray.getString(R.styleable.CustomListView_tv_error_sub_message);
        errorSubColor = typedArray.getColor(R.styleable.CustomListView_clr_error_sub_message, ContextCompat.getColor(context, R.color.text_sub_color));

        loadingMessage = typedArray.getString(R.styleable.CustomListView_tv_loading_message);
        loadingColor = typedArray.getColor(R.styleable.CustomListView_clr_loading_message, ContextCompat.getColor(context, R.color.text_color));

        rootView = LayoutInflater.from(context).inflate(R.layout.moda_list_view, this);

        mTvErrorMsg = rootView.findViewById(R.id.tv_error_message);
        setUpTextView(mTvErrorMsg, errorMessage, errorColor);

        mTvErrorSubMsg = rootView.findViewById(R.id.tv_error_message_sub);
        setUpTextView(mTvErrorSubMsg, errorSubMessage, errorSubColor);

        mTvLoadingMsg = rootView.findViewById(R.id.tv_loading_message);
        setUpTextView(mTvLoadingMsg, loadingMessage, loadingColor);

        mRecycler = rootView.findViewById(R.id.moda_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(context));

        mContainerError = rootView.findViewById(R.id.container_error);
        mBtnErrorAction = rootView.findViewById(R.id.btn_error_action);

        mContainerLoading = rootView.findViewById(R.id.container_message);

    }

    private void setUpTextView(TextView textView, String message, int color) {
        if (Utils.notNullOrEmpty(message))
            textView.setText(message);

        textView.setTextColor(color);

    }

    public void setUpLoadingView(String loadingMessage) {

        mContainerLoading.setVisibility(VISIBLE);
        mTvLoadingMsg.setText(loadingMessage);
        mRecycler.setVisibility(GONE);
        mContainerError.setVisibility(GONE);

    }

    public void setUpErrorView(String errorMessage1, String errorMessage2) {

        mContainerLoading.setVisibility(GONE);
        mRecycler.setVisibility(GONE);
        mContainerError.setVisibility(VISIBLE);
        mBtnErrorAction.setVisibility(VISIBLE);
        mTvErrorMsg.setText(errorMessage1);
        mTvErrorSubMsg.setText(errorMessage2);
        mBtnErrorAction.setOnClickListener(v -> {
            if (mListener != null) mListener.onError();
        });

    }

    public void setUpContentView(boolean isEmpty) {
        if (isEmpty){
            mContainerLoading.setVisibility(GONE);
            mRecycler.setVisibility(GONE);
            mContainerError.setVisibility(VISIBLE);
            mBtnErrorAction.setVisibility(GONE);
            mTvErrorMsg.setText(getContext().getString(R.string.message_empty_list));
            mTvErrorSubMsg.setText("");
        }else {
            mContainerError.setVisibility(GONE);
            mContainerLoading.setVisibility(GONE);
            mRecycler.setVisibility(VISIBLE);
        }
    }
}