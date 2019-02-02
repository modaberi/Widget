package com.modaberi.widget.spinner;

/**
 * Created by smoda on 6/22/2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.modaberi.widget.R;

import java.util.ArrayList;
import java.util.List;

public class ModaSpinner extends RelativeLayout implements
        ModaSpinnerDialog.SpinnerListener {

    private View rootView;
    private TextView tvSelectedItemTitle;
    private TextView tvHint;
    public String hintStr;
    private View spinnerLine;
    private boolean hasHint;
    private List<SpinnerItem> spinnerItemList;
    private FragmentManager mFragmentManager;

    public List<SpinnerItem> getSpinnerItemList() {
        return spinnerItemList;
    }

    public void setSpinnerItemList(List<SpinnerItem> spinnerItemList) {
        this.spinnerItemList = spinnerItemList;
    }

    private boolean isSingleSelect;

    private String spinnerType;

    private String spinnerTag = "CustomSpinner";


    @Override
    public void onItemSelect(SpinnerItem spinnerItem) {
        setSelectedSpinnerItem(spinnerItem);
        if (mListener != null)
            mListener.onUpdateSpinnerItem(spinnerItem);
    }

    @Override
    public void onListSelect(List<SpinnerItem> spinnerItemList) {
        if (mListener != null)
            mListener.onUpdateSpinnerList(spinnerItemList);
    }

    @Override
    public void onAddNewItem(SpinnerItem spinnerItem) {
        if (mListener != null) mListener.onAddNewItem(spinnerItem);
    }

    public interface UpdateSpinnerListener {
        void onUpdateSpinnerItem(SpinnerItem spinnerItem);

        void onUpdateSpinnerList(List<SpinnerItem> spinnerItem);

        void onAddNewItem(SpinnerItem spinnerItem);
    }

    UpdateSpinnerListener mListener;

    public void setListener(UpdateSpinnerListener listener) {
        this.mListener = listener;
    }


    public ModaSpinner(Context context) {
        super(context);
    }

    public ModaSpinner(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData(context, attrs);
    }

    public ModaSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ModaSpinner);
        hintStr = typedArray.getString(R.styleable.ModaSpinner_hint);
        isSingleSelect = typedArray.getBoolean(R.styleable.ModaSpinner_is_single_select, true);
        rootView = LayoutInflater.from(context).inflate(R.layout.moda_spinner, this);
        tvSelectedItemTitle = rootView.findViewById(R.id.tv_spinner_title);
        tvHint = rootView.findViewById(R.id.tv_spinner_hint);
        spinnerLine = rootView.findViewById(R.id.spinner_line);

        hasHint = typedArray.getBoolean(R.styleable.ModaSpinner_has_hint, true);

        initUI(false, false);

        tvSelectedItemTitle.setText(hintStr);

        rootView.setOnClickListener(v -> {

            ModaSpinnerDialog fragment = ModaSpinnerDialog.newInstance(spinnerType, spinnerItemList, isSingleSelect);
            fragment.setListener(this);
            fragment.show(mFragmentManager, spinnerTag);

            showError(false);

        });
    }

    public void showError(boolean isError) {
        if (isError) {
            spinnerLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_red
            ));
        } else {
            spinnerLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
        }
    }

    private void initUI(boolean isSelected, boolean isFocused) {
        if (isSelected && hasHint) {
            tvHint.setText(hintStr);
            tvHint.setVisibility(VISIBLE);
            if (isFocused) {
                tvHint.setTextColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
                spinnerLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
            } else {
                tvHint.setTextColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
                spinnerLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
            }
        } else {
            tvHint.setVisibility(GONE);
            spinnerLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
        }
    }

    public SpinnerItem getSelectedItem() {
        for (SpinnerItem item : spinnerItemList) {
            if (item.isSelected) {
                return item;
            }
        }
        return null;
    }

    public void setSelectedSpinnerItemList(SpinnerItem item) {
        for (SpinnerItem spinnerItem : spinnerItemList) {
            if (spinnerItem == item) {
                spinnerItem.isSelected = true;
                tvSelectedItemTitle.setText(spinnerItem.title);
                tvSelectedItemTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                initUI(true, true);

            }
        }
    }

    public void setSelectedSpinnerItem(SpinnerItem item) {
        for (SpinnerItem spinnerItem : spinnerItemList) {
            if (spinnerItem == item) {
                spinnerItem.isSelected = true;
                tvSelectedItemTitle.setText(spinnerItem.title);
                tvSelectedItemTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
                initUI(true, true);

            } else {
                spinnerItem.isSelected = false;
            }
        }
    }

    public void resetSpinnerItems() {
        spinnerItemList = new ArrayList<>();
        tvSelectedItemTitle.setText(hintStr);
        tvSelectedItemTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.text_sub_color));
    }

    public void setSelectedSpinnerTitle(String title) {
//        for (SpinnerItem spinnerItem : spinnerItemList) {
//            if (title.equals(spinnerItem.title)) {
//                spinnerItem.isSelected = true;
        tvSelectedItemTitle.setText(title);
        tvSelectedItemTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
//            }
//        }
    }

    public void setupSpinner(FragmentManager fragmentManager, List<SpinnerItem> spinnerList, String spinnerType, String spinnerTag) {
        this.spinnerTag = spinnerTag;
        if (spinnerItemList != null) spinnerItemList.clear();
        else spinnerItemList = new ArrayList<>();
        this.spinnerItemList.addAll(spinnerList);
        for (SpinnerItem item : spinnerList) {
            if (item.isSelected)
                setSelectedSpinnerItem(item);
        }
        this.spinnerType = spinnerType;
        this.mFragmentManager = fragmentManager;

    }
}