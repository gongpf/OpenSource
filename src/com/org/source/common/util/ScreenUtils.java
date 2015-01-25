package com.org.source.common.util;

import com.org.source.base.ContextManager;

import android.content.Context;

/**
 * ScreenUtils
 * <ul>
 * <strong>Convert between dp and sp</strong>
 * <li>{@link ScreenUtils#dpToPx(Context, float)}</li>
 * <li>{@link ScreenUtils#pxToDp(Context, float)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-14
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(float dp) {
        return dp * ContextManager.getContext().getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(float px) {
        return px / ContextManager.getContext().getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(float dp) {
        return (int)(dpToPx(dp) + 0.5f);
    }

    public static int pxToDpCeilInt(float px) {
        return (int)(pxToDp(px) + 0.5f);
    }
}
