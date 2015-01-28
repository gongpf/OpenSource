package com.org.source.widget.menudrawer;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

final public class ViewHelper {

    private ViewHelper() {
    }

    public static int getLeft(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getLeft() + v.getTranslationX());
        }

        return v.getLeft();
    }

    public static int getTop(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getTop() + v.getTranslationY());
        }

        return v.getTop();
    }

    public static int getRight(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getRight() + v.getTranslationX());
        }

        return v.getRight();
    }

    public static int getBottom(View v) {
        if (MenuDrawer.USE_TRANSLATIONS) {
            return (int) (v.getBottom() + v.getTranslationY());
        }

        return v.getBottom();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getLayoutDirection(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return v.getLayoutDirection();
        }

        return View.LAYOUT_DIRECTION_LTR;
    }
}
