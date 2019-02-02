package com.modaberi.widget.stepper;

/**
 * Created by smoda on 6/22/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.modaberi.widget.ModaRippleView;
import com.modaberi.widget.R;
import com.modaberi.widget.Utils;
import com.modaberi.widget.stepper.StepperHeader.EnumStep;

import java.util.ArrayList;
import java.util.List;

public class ModaStepper extends RelativeLayout implements StepperAdapter.OnStepperItemClickListener {

    private RecyclerView mRecycler;
    private StepperAdapter mRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<StepperHeader> stepperHeaderList = new ArrayList<>();
    private FragmentManager mFragmentManager;

    public interface OnUpdateListener {
        void onError();

        void onclickNext(int nextPage, Fragment currentFragment, Fragment nextFragment);

        void onClickBack(int backPage);
    }

    private OnUpdateListener mListener;

    public void setListener(OnUpdateListener listener) {
        this.mListener = listener;
    }

    public ModaStepper(Context context) {
        super(context);
    }

    public ModaStepper(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData(context, attrs);
    }

    public ModaStepper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.moda_stepper, this);
        setupUI(rootView);

    }

    public boolean isFirstPage() {
        if (stepperHeaderList != null && stepperHeaderList.size() > 0) {
            return stepperHeaderList.get(0).step.equals(EnumStep.CURRENT);
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.empty_step_list), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isLastPage() {
        if (stepperHeaderList != null && stepperHeaderList.size() > 0) {
            return stepperHeaderList.get(fragmentList.size() - 1).step.equals(EnumStep.CURRENT);
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.empty_step_list), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private int getPageSize() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

    private int getCurrentPage() {
        for (int i = 0; i < stepperHeaderList.size(); i++) {

        }

        for (StepperHeader stepperHeader : stepperHeaderList) {
            if (stepperHeader.step.methodName.equals(EnumStep.CURRENT.methodName)) {
                return stepperHeader.position;
            }
        }
        return 0;
    }

    public int getNextPage() {
        if (fragmentList != null && getCurrentPage() + 1 < fragmentList.size()) {
            return getCurrentPage() + 1;
        } else {
            Toast.makeText(getContext(), "گام آخر است!", Toast.LENGTH_LONG).show();
            return 1000;
        }
    }

    public int getPrevPage() {
        if (fragmentList != null && getCurrentPage() - 1 > -1) {
            return getCurrentPage() - 1;
        } else {
            Toast.makeText(getContext(), "گام اول است!", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private void setupUI(View view) {
        ModaRippleView mBtnNext = view.findViewById(R.id.btn_next);
        mBtnNext.setOnRippleCompleteListener(rippleView -> {

            new android.os.Handler().postDelayed(() -> {
                if (getContext() != null) Utils.hideKeyboard((Activity) getContext());
                if (fragmentList != null && isLastPage()) {
                    Toast.makeText(getContext(), "گام آخر است!!!", Toast.LENGTH_LONG).show();
                } else {

                    //set listener before setCurrent item Of viewpager
                    if (mListener != null && fragmentList != null)
                        mListener.onclickNext(getNextPage(),
                                fragmentList.get(getCurrentPage()),
                                fragmentList.get(getNextPage()));
                }
            }, 110);

        });

        ModaRippleView mBtnPrev = view.findViewById(R.id.btn_before);
        mBtnPrev.setOnRippleCompleteListener(rippleView -> {

            new android.os.Handler().postDelayed(() -> {
                if (getContext() != null) Utils.hideKeyboard((Activity) getContext());

                if (fragmentList != null && isFirstPage()) {
                    Toast.makeText(getContext(), "گام اول است!!!", Toast.LENGTH_LONG).show();
                } else {
                    //set listener before setCurrent item Of viewpager
                    if (mListener != null)
                        mListener.onClickBack(getPrevPage());
                }
            }, 110);

        });

        mRecycler = view.findViewById(R.id.recycler_step);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRecycler.setLayoutManager(mLayoutManager);

    }

    public void setStepperLists(List<StepperItem> stepperItemList,
                                FragmentManager fragmentManager, OnUpdateListener listener) {

        for (int i = 0; i < stepperItemList.size(); i++) {
            StepperItem item = stepperItemList.get(i);
            stepperHeaderList.add(new StepperHeader(item.header, i, i == 0 ? EnumStep.CURRENT : EnumStep.NEXT));
            fragmentList.add(item.fragment);
        }

        mRecyclerAdapter = new StepperAdapter(getContext(), stepperHeaderList, this);
        mRecycler.setAdapter(mRecyclerAdapter);

        mFragmentManager = fragmentManager;
        setUpNewPage(0, fragmentManager);

        setListener(listener);
    }

    public void setUpNewPage(int position, FragmentManager mFragmentManager) {
        if (fragmentList != null && fragmentList.size() > 0) {

            Fragment fragment = fragmentList.get(position);
            Utils.replaceFragment(
                    mFragmentManager,
                    R.id.container_stepper,
                    fragment,
                    fragment.getClass().getName(),
                    fragment.getClass().getName());

            updateCurrentStepUI(position);

        } else
            Toast.makeText(getContext(), getContext().getString(R.string.init_fragment_list), Toast.LENGTH_SHORT).show();
    }

    private void updateCurrentStepUI(int newPos) {

        if (mRecyclerAdapter != null) {

            if (stepperHeaderList != null) {

                for (int i = 0; i < stepperHeaderList.size(); i++) {

                    if (i == newPos)
                        stepperHeaderList.get(i).step = EnumStep.CURRENT;
                    else if (i < newPos)
                        stepperHeaderList.get(i).step = EnumStep.DONE;
                    else
                        stepperHeaderList.get(i).step = EnumStep.NEXT;
                }

                mRecyclerAdapter.setStepperHeaderList(stepperHeaderList);
                mLayoutManager.scrollToPositionWithOffset(newPos, 0);
            }
        }
    }

    @Override
    public void onItemClick(int clickedItemPosition) {
        setUpNewPage(clickedItemPosition, mFragmentManager);
    }

}