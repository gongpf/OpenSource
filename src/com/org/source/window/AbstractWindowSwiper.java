package com.org.source.window;

import android.view.MotionEvent;

public abstract class AbstractWindowSwiper implements IWindowAnimator
{
    public abstract class WindowSwipeListener
    {
        public void onInit()
        {
        }

        public void onStart()
        {
        }

        public void onMove(float dx, float dy)
        {
        }

        public void onEnd()
        {
        }
    }
    
    private WindowSwipeListener mWindowSwipeListener;

    protected enum Direction {NONE, LEFT, RIGHT, DOWN, UP};
    protected Direction mDirection = Direction.NONE;

    private float mLastPointX;
    private float mLastPointY;
    
    protected float mDownPointX;
    protected float mDownPointY;
    
    public void setWindowSwipeListener(WindowSwipeListener l)
    {
        mWindowSwipeListener = l;
    }
    
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return false;
    }
    
    public void handleDownEvent(MotionEvent event)
    {
        mDirection = Direction.NONE;
        updateLastPoint(event);
        updateDownPoint(event);
        
        if (null != mWindowSwipeListener)
        {
            mWindowSwipeListener.onInit();
        }
    }
    
    public void handleMoveEvent(MotionEvent event)
    {
        float dx = getDetalX(event);
        float dy = getDetalY(event);

        if (mDirection == Direction.NONE && Math.sqrt(dx * dx + dy * dy) > 8)
        {
            initDirection(dx, dy);

            if (null != mWindowSwipeListener)
            {
                mWindowSwipeListener.onStart();
            }
        }

        if (null != mWindowSwipeListener)
        {
            mWindowSwipeListener.onMove(dx, dy);
        }
    }

    public void handleUpOrCancelEvent()
    {
        if (null != mWindowSwipeListener)
        {
            mWindowSwipeListener.onEnd();
        }
    }

    private void initDirection(float dx, float dy)
    {
        if (Math.abs(dx) > Math.abs(dy))
        {
            mDirection = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        }
        else
        {
            mDirection = dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    
    protected void updateLastPoint(MotionEvent event)
    {
        mLastPointX = event.getX();
        mLastPointY = event.getY();
    }
    
    protected void updateDownPoint(MotionEvent event)
    {
        mDownPointX = event.getX();
        mDownPointY = event.getY();
    }
    
    protected float getDetalY(MotionEvent event)
    {
        return event.getY() - mLastPointY; 
    }

    protected float getDetalX(MotionEvent event)
    {
        return event.getX() - mLastPointX; 
    }
}
