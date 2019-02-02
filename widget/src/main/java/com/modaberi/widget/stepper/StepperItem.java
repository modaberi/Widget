package com.modaberi.widget.stepper;

import android.support.v4.app.Fragment;

public class StepperItem{

    Fragment fragment;
    String header;

    public StepperItem(Fragment fragment, String header) {
        this.fragment = fragment;
        this.header = header;
    }
}