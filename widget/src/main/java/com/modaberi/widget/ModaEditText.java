package com.modaberi.widget;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ModaEditText extends LinearLayout {

    private EditText mEditText;
    private RecyclerView mRecycler;
    private ModaEditTextAdapter mAdapter;

    private List<KeyValue> itemList;

    public ModaEditText(Context context) {
        super(context);
    }

    public ModaEditText(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData(context, attrs);
    }

    public ModaEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ModaEditText);
        String hintStr = typedArray.getString(R.styleable.ModaEditText_edit_text_hint);

        View rootView = LayoutInflater.from(context).inflate(R.layout.moda_edittext, this);

        mRecycler = rootView.findViewById(R.id.moda_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ModaEditTextAdapter(context);
        mRecycler.setAdapter(mAdapter);

        mEditText = rootView.findViewById(R.id.moda_edit_text);
        mEditText.setHint(hintStr);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) {
                    mRecycler.setVisibility(GONE);
                } else {
                    mRecycler.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void setupModaEditText(List<KeyValue> itemList) {
        this.itemList = itemList;
        if (mAdapter != null) mAdapter.setItemList(itemList);
    }

    public boolean checkIsValid() {
        if (Utils.notNullOrEmpty(mEditText.getText().toString())) {
            return true;
        } else {
            mEditText.setError(getContext().getString(R.string.message_empty_list));
            return false;

        }
    }

    public KeyValue getSelectedItem() {
        for (KeyValue item : itemList) {
            if (item.isSelected) {
                return item;
            }
        }
        if (Utils.notNullOrEmpty(mEditText.getText().toString())) {
            KeyValue keyValue = new KeyValue();
            keyValue.isSelected = true;
            keyValue.itemValue = mEditText.getText().toString();

            return keyValue;
        } else

            return null;
    }

    public void setSelectedSpinnerItem(KeyValue selectedItem, List<KeyValue> itemList) {
        for (KeyValue item : itemList) {
            if (selectedItem.itemValue.equals(item.itemValue)) {
                item.isSelected = true;
                mEditText.setText(item.itemValue);
                mRecycler.setVisibility(GONE);

            } else {
                item.isSelected = false;
            }
        }

    }

    public class ModaEditTextAdapter extends RecyclerView.Adapter<ModaEditTextAdapter.ModaEditTextVH> {
        private Context context;
        private List<KeyValue> itemList;

        ModaEditTextAdapter(Context context) {
            this.context = context;
        }

        private void setItemList(List<KeyValue> items) {
            itemList = new ArrayList<>();
            itemList.addAll(items);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ModaEditTextVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(context).inflate(R.layout.moda_list_item_edit_text, parent, false);
            return new ModaEditTextVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ModaEditTextVH holder, int position) {

            KeyValue item = itemList.get(position);
            holder.mTvTitle.setText(item.itemValue);
            holder.mLine.setVisibility(position + 1 == itemList.size() ? View.GONE : View.VISIBLE);
            holder.mTvTitle.setTextColor(item.isSelected ? ContextCompat.getColor(getContext(), R.color.colorAccent) : ContextCompat.getColor(getContext(), R.color.text_color));
            holder.mItemContainer.setOnRippleCompleteListener(rippleView -> setSelectedSpinnerItem(item, itemList));

        }

        @Override
        public int getItemCount() {
            return itemList != null ? itemList.size() : 0;
        }

        class ModaEditTextVH extends RecyclerView.ViewHolder {

            private ModaRippleView mItemContainer;
            private TextView mTvTitle;
            private View mLine;

            ModaEditTextVH(View contentView) {
                super(contentView);
                mItemContainer = contentView.findViewById(R.id.container_moda_edit_text_item);
                mTvTitle = contentView.findViewById(R.id.moda_tv_title);
                mLine = contentView.findViewById(R.id.moda_edit_text_line);
            }
        }
    }


}