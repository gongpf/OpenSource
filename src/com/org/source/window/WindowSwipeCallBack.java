package com.org.source.window;

public interface WindowSwipeCallBack
{
    public void onInit();

    public void onStart();

    public void onMove(float dx, float dy);

    public void onEnd();
}
