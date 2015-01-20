package com.org.source.window;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.org.source.eventbus.EventBus;
import com.org.source.nineoldandroids.animation.Animator;
import com.org.source.window.WindowManager.WindowEvent;

public class Window extends FrameLayout implements IWindowAnimator
{
    private final FrameLayout mContentLayer;
    private AbstractWindowSwiper mWindowSwiper;

    public Window(Context context)
    {
        super(context);
        
        mContentLayer = new FrameLayout(context);
        super.addViewInLayout(mContentLayer, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        setBackgroundColor(Color.TRANSPARENT);
        mContentLayer.setBackgroundColor(Color.TRANSPARENT);
        
        mWindowSwiper = new DefaultWindowSwiper(mContentLayer);
    }
    
    public void setContentView(View view, LayoutParams params)
    {
        mContentLayer.removeAllViews();
        mContentLayer.addView(view, params);
    }
    
    public void setWindowSwiper(AbstractWindowSwiper swiper)
    {
        mWindowSwiper = swiper;
    }
    
    @Override
    public void addView(View child)
    {
        throw new UnsupportedOperationException("Cannot add view from outside.");
    }
    
    @Override
    public void addView(View child, int index)
    {
        throw new UnsupportedOperationException("Cannot add view from outside.");
    }
    
    @Override
    public void addView(View child, int width, int height)
    {
        throw new UnsupportedOperationException("Cannot add view from outside.");
    }
    
    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params)
    {
        throw new UnsupportedOperationException("Cannot add view from outside.");
    }
    
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params)
    {
        throw new UnsupportedOperationException("Cannot add view from outside.");
    }
    
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (null != mWindowSwiper)
        {
            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    mWindowSwiper.handleDownEvent(event);
                    break;

                default:
                    break;
            }
        }
        
        return super.dispatchTouchEvent(event);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return null != mWindowSwiper ? mWindowSwiper.onInterceptTouchEvent(ev) : super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (null == mWindowSwiper)
        {
            return super.onTouchEvent(event);
        }
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mWindowSwiper.handleDownEvent(event);
                break;

            case MotionEvent.ACTION_MOVE:
                mWindowSwiper.handleMoveEvent(event);
                break;
                
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mWindowSwiper.handleUpOrCancelEvent();
                break;
            
            default:
                break;
        }
        
        return true;
    }
    
    @Override
    public Animator getPushAnimator()
    {
        return null != mWindowSwiper ? mWindowSwiper.getPushAnimator() : null;
    }
    
    @Override
    public Animator getPopupAnimator()
    {
        return null != mWindowSwiper ? mWindowSwiper.getPopupAnimator() : null;
    }
    
    @Override
    public boolean isAnimation()
    {
        return null != mWindowSwiper ? mWindowSwiper.isAnimation() : false;
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_UP 
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK)
        {
            popupWindow();
            return true;
        }
        
        return false;
    }
    
    private void popupWindow()
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.POPUPWINDOW;
        EventBus.getDefault().post(windowEvent);
    }
}
