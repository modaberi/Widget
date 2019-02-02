package com.modaberi.widget;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.List;

public class Utils {

    public static boolean notNullOrEmpty(String str) {
        return str != null && !"".equals(str);
    }

    public static boolean notNullOrEmpty(List<String> stringList) {
        return stringList == null || stringList.size() == 0;
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void replaceFragment(FragmentManager manager,
                                       int containerId,
                                       Fragment fragment,
                                       String tag,
                                       String backStack) {

        Fragment fragmentByTag = manager.findFragmentByTag(tag);
        manager.beginTransaction()
                .replace(containerId, fragmentByTag != null ? fragmentByTag : fragment, tag)
                .addToBackStack(backStack)
                .commit();
    }

    public static void setClickedTextAction(final TextView textView,
                                            int activeColorId,
                                            final int defaultColorId) {

        textView.setTextColor(activeColorId);
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

        new Handler().postDelayed(() -> {
            textView.setTextColor(defaultColorId);
//             textView.setTypeface(textView.getTypeface(), Typeface.NORMAL);
        }, 150);
    }

}
