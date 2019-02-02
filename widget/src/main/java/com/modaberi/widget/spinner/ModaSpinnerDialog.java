package com.modaberi.widget.spinner;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.modaberi.widget.ModaRippleView;
import com.modaberi.widget.R;
import com.modaberi.widget.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModaSpinnerDialog extends DialogFragment {

    private static final String SPINNER_LIST = "SPINNER_LIST";
    private static final String SPINNER_TYPE = "SPINNER_TYPE";
    private static final String SPINNER_IS_SINGLE_SELECT = "SPINNER_IS_SINGLE_SELECT";

    private SpinnerListener listener;

    public void setListener(SpinnerListener listener) {
        this.listener = listener;
    }

    public interface SpinnerListener {
        void onItemSelect(SpinnerItem spinnerItem);

        void onListSelect(List<SpinnerItem> spinnerItemList);

        void onAddNewItem(SpinnerItem spinnerItem);
    }

    public static ModaSpinnerDialog newInstance(String spinnerType, List<SpinnerItem> spinnerItemList, boolean isSingleSelect) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SPINNER_LIST, (ArrayList<? extends Parcelable>) spinnerItemList);
        bundle.putString(SPINNER_TYPE, spinnerType);
        bundle.putBoolean(SPINNER_IS_SINGLE_SELECT, isSingleSelect);

        ModaSpinnerDialog fragment = new ModaSpinnerDialog();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        // safety check
        if (getDialog() == null) {
            return;
        }
        WindowManager.LayoutParams params;
        if (getDialog().getWindow() != null) {
            params = getDialog().getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(params);

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mRootView = inflater.inflate(R.layout.moda_dialog_spinner, container, false);

        if (getDialog().getWindow() != null) {
                getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }

            String strSpinnerType = "";
            List<SpinnerItem> itemList = new ArrayList<>();
            boolean isSingleSelect = true;

            if (getArguments() != null) {
                itemList.addAll(Objects.requireNonNull(getArguments().getParcelableArrayList(SPINNER_LIST)));
                strSpinnerType = getArguments().getString(SPINNER_TYPE);
                isSingleSelect = getArguments().getBoolean(SPINNER_IS_SINGLE_SELECT);
            }

            setupUI(mRootView, itemList, strSpinnerType, isSingleSelect);

            return mRootView;
        }

        private void setupUI (View mRootView, List< SpinnerItem > itemList, String strSpinnerType,
                              boolean isSingleSelect){
//        TextView mTvTitle = mRootView.findViewById(R.id.tv_spinner_header);
//        mTvTitle.setText(strSpinnerType);

//        mRootView.findViewById(R.id.btn_close_dialog).setOnClickListener(v -> dismiss());

            ModaRippleView mRvCancel = mRootView.findViewById(R.id.rv_cancel);
            mRvCancel.setOnRippleCompleteListener(v -> dismiss());

            RecyclerView mRecycler = mRootView.findViewById(R.id.recycler_bbottoomsheet_spinner);
            ModaSpinnerAdapter mAdapter = new ModaSpinnerAdapter(getContext(), itemList, isSingleSelect);
            mRecycler.setAdapter(mAdapter);

            TextInputEditText mEtSearch = mRootView.findViewById(R.id.et_search);
            mEtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<SpinnerItem> searchedList = new ArrayList<>();
                    new Handler().postDelayed(() -> {
                        for (SpinnerItem spinnerItem : itemList) {
                            if (spinnerItem.title.contains(mEtSearch.getText().toString())) {
                                searchedList.add(spinnerItem);
                            }
                        }
                        ModaSpinnerAdapter mAdapter = new ModaSpinnerAdapter(getContext(), searchedList, isSingleSelect);
                        mRecycler.setAdapter(mAdapter);

                    }, 50);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            if (!isSingleSelect) {
                mRootView.findViewById(R.id.container_new_item).setVisibility(View.VISIBLE);

                ModaRippleView mRvSelectItems = mRootView.findViewById(R.id.rv_select_items);
                mRvSelectItems.setVisibility(View.VISIBLE);
                mRvSelectItems.setOnRippleCompleteListener(rippleView -> {

                    List<SpinnerItem> spinnerItemList = mAdapter.getData();
                    List<SpinnerItem> selectedItemList = new ArrayList<>();
                    for (SpinnerItem spinnerItem : spinnerItemList) {
                        if (spinnerItem.isSelected) {
                            selectedItemList.add(spinnerItem);
                        }
                    }

                    new Handler().postDelayed(() -> {
                        listener.onListSelect(selectedItemList);
                        dismiss();
                    }, 100);
                });

                AppCompatEditText mEtNewItem = mRootView.findViewById(R.id.et_new_item);
                ImageView mBtnAddNewItem = mRootView.findViewById(R.id.btn_add_new_item);
                mBtnAddNewItem.setOnClickListener(v -> {
                    if (Utils.notNullOrEmpty(mEtNewItem.getText().toString())) {

                        long index = 0;
                        if (itemList != null)
                            index = itemList.size() + 1;

                        SpinnerItem spinnerItem = new SpinnerItem(mEtNewItem.getText().toString(), index, true);
                        itemList.add(spinnerItem);
                        mAdapter.swapData(itemList);

                        mEtNewItem.setText("");

                        if (listener != null) listener.onAddNewItem(spinnerItem);

                    } else {
                        mEtNewItem.setError(getString(R.string.add_someting));
                    }
                });
            }
        }


        public class ModaSpinnerAdapter extends RecyclerView.Adapter<ModaSpinnerAdapter.SpinnerItemVH> {
            private Context context;
            private List<SpinnerItem> itemList;
            private boolean isSingleSelect;

            public ModaSpinnerAdapter(Context context,
                                      List<SpinnerItem> itemList,
                                      boolean isMultiSelect) {
                this.context = context;
                this.itemList = itemList;
                this.isSingleSelect = isMultiSelect;
            }

            public List<SpinnerItem> getData() {
                return itemList;
            }

            public void swapData(List<SpinnerItem> items) {

                itemList = new ArrayList<>();
                itemList.addAll(items);
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ModaSpinnerAdapter.SpinnerItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(context).inflate(R.layout.moda_list_item_spinner, parent, false);
                return new ModaSpinnerAdapter.SpinnerItemVH(view);

            }

            @Override
            public void onBindViewHolder(@NonNull ModaSpinnerAdapter.SpinnerItemVH holder, int position) {
               /* SpinnerItem item = itemList.get(position);
                holder.binding.setItem(item);
                holder.binding.executePendingBindings();

                holder.binding.cbMultiSelect.setVisibility(isSingleSelect ? View.GONE : View.VISIBLE);
                if (item.isSelected && !isSingleSelect)
                    holder.binding.cbMultiSelect.setChecked(true);
                holder.binding.cbMultiSelect.setOnCheckedChangeListener((buttonView, isChecked) -> item.isSelected = isChecked);

                holder.itemView.setOnClickListener(v -> {
                    if (isSingleSelect) {
                        if (listener != null)
                            Utils.setClickedTextAction(holder.mTvTitle,
                                    ContextCompat.getColor(context, R.color.colorPrimary),
                                    ContextCompat.getColor(context, R.color.text_sub_color));
                        new Handler().postDelayed(() -> {
                            listener.onItemSelect(item);
                            dismiss();
                        }, 100);
                    } else {
                        if (holder.binding.cbMultiSelect.isChecked()) {
                            holder.binding.cbMultiSelect.setChecked(false);
                            item.isSelected = false;
                        } else {
                            holder.binding.cbMultiSelect.setChecked(true);
                            item.isSelected = true;
                        }
                    }

                });*/
            }

            @Override
            public int getItemCount() {
                return itemList != null ? itemList.size() : 0;
            }

            class SpinnerItemVH extends RecyclerView.ViewHolder {

                TextView mTvTitle;

                SpinnerItemVH(View view) {
                    super(view);
                     mTvTitle = view.findViewById(R.id.tv_item_title);
                }
            }
        }

    }



