package com.org.source.main;

import android.app.Activity;
import android.content.Context;

import com.org.source.base.ContextManager;
import com.org.source.event.ISystemEventHandler;
import com.org.source.event.ISystemEventHandler.SystemEvent;
import com.org.source.eventbus.EventBus;
import com.org.source.window.WindowManager;

public class Controller
{
    private WindowManager mWindowManager;

    public Controller(Context context)
    {
        init(context);
    }
    
    private void init(Context context)
    {
        ContextManager.initialize(context);
    }

    public void start(Activity activity)
    {
        mWindowManager = new WindowManager(activity);
        mWindowManager.pushWindow(new HomeWindow(activity));
    }

    public void destory()
    {
        ContextManager.destroy();

        SystemEvent systemEvent = new SystemEvent();
        systemEvent.mEventType = ISystemEventHandler.EventType.DESTORY;
        EventBus.getDefault().post(systemEvent);
    }
}
