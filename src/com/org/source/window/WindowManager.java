package com.org.source.window;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.org.source.event.ISystemEventHandler;
import com.org.source.eventbus.EventBus;
import com.org.source.nineoldandroids.animation.Animator;

public class WindowManager implements ISystemEventHandler
{
    public enum EventType {UNKOWN, PUSHWINDOW, POPUPWINDOW};
    
    public static class WindowEvent
    {
        public EventType mEventType = EventType.UNKOWN;
        public Object mObject;
    }
    
    private class ViewRoot extends FrameLayout
    {
        public ViewRoot(Context context)
        {
            super(context);
        }
        
        @Override
        public boolean dispatchKeyEvent(KeyEvent event)
        {
            return mWindowStack.size() > 0 ? mWindowStack.peek().dispatchKeyEvent(event) 
                    : super.dispatchKeyEvent(event);
        }
    }
    
    private final Stack<Window> mWindowStack;
    private final ViewRoot mViewRoot;
    
    public WindowManager(Activity attachActivity)
    {
        mWindowStack = new Stack<Window>();
        mViewRoot = new ViewRoot(attachActivity);
        mViewRoot.setFocusableInTouchMode(true);
        attachActivity.setContentView(mViewRoot);
        EventBus.getDefault().register(this);
    }
    
    public void pushWindow(Window win)
    {
        final Window window = win;

        if (window.isAnimation())
        {
            return ;
        }
        
        mViewRoot.addView(window);
        mWindowStack.add(window);
        
        Animator animator = window.getPushAnimator();
        
        if (null == animator)
        {
            window.onWindowAttached();
            return ;
        }

        animator.addListener(new AbstractAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                window.onWindowAttached();
                animation.removeListener(this);
            }
        });
        animator.start();
    }
    
    public void popWindow()
    {
        final Window window = mWindowStack.pop();
        
        if (window.isAnimation())
        {
            return ;
        }
        
        Animator animator = window.getPopupAnimator();
        
        if (null == animator)
        {
            mViewRoot.removeView(window);
            window.onWindowDetached();
            return;
        }
        
        animator.addListener(new AbstractAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mViewRoot.removeView(window);
                window.onWindowDetached();
                animation.removeListener(this);
            }
        });
        animator.start();
    }
    
    public void clear()
    {
        mViewRoot.removeAllViews();
        mWindowStack.clear();
    }
    
    public void onEventMainThread(WindowEvent event)
    {
        if (null == event)
        {
            return ;
        }
        
        switch(event.mEventType)
        {
            case PUSHWINDOW:
                pushWindow((Window)event.mObject);
                break;
                
            case POPUPWINDOW:
                popWindow();
                break;

            default:
                break;
        }
    }

    @Override
    public void onEvent(SystemEvent event)
    {
        switch (event.mEventType)
        {
            case DESTORY:
                EventBus.getDefault().unregister(this);
                break;

            default:
                break;
        }
    }
}
