package com.org.source.window;

import com.org.source.eventbus.EventBus;
import com.org.source.widget.menudrawer.ViewHelper;
import com.org.source.window.WindowManager.WindowEvent;

import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractWindowSwiper implements WindowSwipeCallBack, IWindowAnimator
{
    public interface OnInterceptMoveEventListener 
    {
        boolean isViewDraggableVertically(View v, int dy, int x, int y);

        boolean isViewDraggableHorizontally(View v, int dx, int x, int y);
    }

    protected enum Direction {NONE, LEFT, RIGHT, DOWN, UP};
    protected Direction mDirection = Direction.NONE;

    private float mLastPointX;
    private float mLastPointY;
    
    protected float mDownPointX;
    protected float mDownPointY;

    protected OnInterceptMoveEventListener mOnInterceptMoveEventListener; 
    private boolean mIsDragging = false;
    
    protected final View mTargetView;
    
    public AbstractWindowSwiper(View target)
    {
        mTargetView = target;
    }
    
    public void setInterceptMoveEventListener(OnInterceptMoveEventListener l)
    {
        mOnInterceptMoveEventListener = l;
    }
    
    public void handleDownEvent(MotionEvent event)
    {
        mDirection = Direction.NONE;
        mIsDragging = false;

        updateLastPoint(event);
        updateDownPoint(event);
        
        onInit();
    }
    
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                float dx = getDetalX(event);
                float dy = getDetalY(event);
                float x = event.getX();
                float y = event.getY();
                if (mDirection == Direction.NONE && Math.sqrt(dx * dx + dy * dy) > 8)
                {
                    initDirection(dx, dy);

                    if (mOnInterceptMoveEventListener != null 
                            && canChildrenScroll((int) dx, (int) dy, (int)x, (int)y))
                    {
                        return false;
                    }
                    mIsDragging = true;
                    onStart();
                }

                break;

            default:
                break;
        }

        return mIsDragging;
    }
    
    public void handleMoveEvent(MotionEvent event)
    {
        float dx = getDetalX(event);
        float dy = getDetalY(event);

        onMove(dx, dy);
    }

    public void handleUpOrCancelEvent()
    {
        onEnd();
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
    
    public void popupWindow()
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.POPUPWINDOW;
        EventBus.getDefault().post(windowEvent); 
    }
    
    public void exitWindow()
    {
        popupWindow();
    }
    
    protected boolean canChildrenScroll(int dx, int dy, int x, int y)
    {
        boolean canScroll = false;

        switch (mDirection)
        {
            case LEFT:
            case RIGHT:
                canScroll = canChildScrollHorizontally(mTargetView, false, dx, x - ViewHelper.getLeft(mTargetView), y - ViewHelper.getTop(mTargetView));
                break;

            case UP:
            case DOWN:
                canScroll = canChildScrollVertically(mTargetView, false, dy, x - ViewHelper.getLeft(mTargetView), y - ViewHelper.getTop(mTargetView));

            default:
                break;
        }

        return canScroll;
    }
    
    protected boolean canChildScrollHorizontally(View v, boolean checkV, int dx, int x, int y)
    {
        if (v instanceof ViewGroup)
        {
            final ViewGroup group = (ViewGroup) v;

            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance
            // first.
            for (int i = count - 1; i >= 0; i--)
            {
                final View child = group.getChildAt(i);

                final int childLeft = child.getLeft() + supportGetTranslationX(child);
                final int childRight = child.getRight() + supportGetTranslationX(child);
                final int childTop = child.getTop() + supportGetTranslationY(child);
                final int childBottom = child.getBottom() + supportGetTranslationY(child);

                if (x >= childLeft && x < childRight && y >= childTop && y < childBottom && canChildScrollHorizontally(child, true, dx, x - childLeft, y - childTop))
                {
                    return true;
                }
            }
        }

        return checkV && mOnInterceptMoveEventListener.isViewDraggableHorizontally(v, dx, x, y);
    }

    protected boolean canChildScrollVertically(View v, boolean checkV, int dx, int x, int y)
    {
        if (v instanceof ViewGroup)
        {
            final ViewGroup group = (ViewGroup) v;

            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance
            // first.
            for (int i = count - 1; i >= 0; i--)
            {
                final View child = group.getChildAt(i);

                final int childLeft = child.getLeft() + supportGetTranslationX(child);
                final int childRight = child.getRight() + supportGetTranslationX(child);
                final int childTop = child.getTop() + supportGetTranslationY(child);
                final int childBottom = child.getBottom() + supportGetTranslationY(child);

                if (x >= childLeft && x < childRight && y >= childTop && y < childBottom && canChildScrollVertically(child, true, dx, x - childLeft, y - childTop))
                {
                    return true;
                }
            }
        }

        return checkV && mOnInterceptMoveEventListener.isViewDraggableVertically(v, dx, x, y);
    }

    private int supportGetTranslationY(View v)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            return (int) v.getTranslationY();
        }

        return 0;
    }

    private int supportGetTranslationX(View v)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            return (int) v.getTranslationX();
        }

        return 0;
    }
}
