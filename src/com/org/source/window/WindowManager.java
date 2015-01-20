package com.org.source.window;

import java.util.Stack;

import android.app.Activity;
import android.view.ViewGroup;
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
    
    private final Stack<Window> mWindowStack;
    private final ViewGroup mViewRoot;
    
    public WindowManager(Activity attachActivity)
    {
        mWindowStack = new Stack<Window>();
        mViewRoot = new FrameLayout(attachActivity);
        attachActivity.setContentView(mViewRoot);
        EventBus.getDefault().register(this);
    }
    
    public void pushWindow(Window window)
    {
        if (window.isAnimation())
        {
            return ;
        }
        
        mViewRoot.addView(window);
        mWindowStack.add(window);
        
        Animator animator = window.getPushAnimator();
        if (null != animator)
        {
            animator.start();
        }
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
            return;
        }
        
        animator.addListener(new AbstractAnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mViewRoot.removeView(window);
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
