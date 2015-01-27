package com.org.source.widget.ViewPager;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class ViewCompat
{
    interface ViewCompatImpl {

        public void postInvalidateOnAnimation(View view);

        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom);

        public void postOnAnimation(View view, Runnable action);

        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis);
    }
    
    static final ViewCompatImpl IMPL;
    static {

        final int version = android.os.Build.VERSION.SDK_INT;

        if (version >= 16) {
            IMPL = new ViewCompatJB();
        } else {
            IMPL = new ViewCompatICS();
        }
    }
    
    public static class ViewCompatICS implements ViewCompatImpl
    {
        @Override
        public void postInvalidateOnAnimation(View view) {
            view.postInvalidateDelayed(getFrameTime());
        }

        @Override
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            view.postInvalidateDelayed(getFrameTime(), left, top, right, bottom);
        }

        @Override
        public void postOnAnimation(View view, Runnable action) {
            view.postDelayed(action, getFrameTime());
        }

        @Override
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            view.postDelayed(action, getFrameTime() + delayMillis);
        }

        long getFrameTime() {
            return 10;
        }
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static class ViewCompatJB implements ViewCompatImpl
    {
        @Override
        public void postInvalidateOnAnimation(View view) {
            view.postInvalidateOnAnimation();
        }

        @Override
        public void postInvalidateOnAnimation(View view, int left, int top,
                int right, int bottom) {
            view.postInvalidate(left, top, right, bottom);
        }

        @Override
        public void postOnAnimation(View view, Runnable action) {
            view.postOnAnimation(action);
        }

        @Override
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            view.postOnAnimationDelayed(action, delayMillis);
        }
    }

    public static void postInvalidateOnAnimation(View view)
    {
        IMPL.postInvalidateOnAnimation(view);
    }

    public static void postOnAnimation(View view, Runnable runnable)
    {
        IMPL.postOnAnimation(view, runnable);
    }

}
