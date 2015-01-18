package com.org.source;

import android.content.Context;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.org.source.eventbus.EventBus;
import com.org.source.window.WindowManager;
import com.org.source.window.WindowManager.WindowEvent;


public class RSSController
{
    public enum EventType {UNKOWN, OPENCONTENTWINDOW, POPUPWINDOW};
    
    public static class RSSEvent
    {
        public EventType mEventType = EventType.UNKOWN;
        public Object mObject;
    }
    
    private RSSListWindow mListWindow;
    private RSSContentWindow mContentWindow;
    private final Context mContext;
    
    public RSSController(Context context)
    {
        mContext = context;
        EventBus.getDefault().register(this);
    }
    
    public void openListWindow(String url)
    {
        getListWindow().loadUrl(url);
        
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        windowEvent.mObject = getListWindow();
        EventBus.getDefault().post(windowEvent);
    }
    
    private RSSListWindow getListWindow()
    {
        if (null == mListWindow)
        {
            mListWindow = new RSSListWindow(mContext);
        }
        
        return mListWindow;
    }
    
    private RSSContentWindow getContentWindow()
    {
        if (null == mContentWindow)
        {
            mContentWindow = new RSSContentWindow(mContext);
        }
        
        return mContentWindow;
    }
    
    public void onEventMainThread(RSSEvent event)
    {
        if (null == event)
        {
            return ;
        }
        
        switch(event.mEventType)
        {
            case OPENCONTENTWINDOW:
                if (null != event.mObject)
                {
                    WindowEvent windowEvent = new WindowEvent();
                    windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
                    getContentWindow().loadData((SyndEntry)event.mObject);
                    windowEvent.mObject = getContentWindow();
                    EventBus.getDefault().post(windowEvent);
                }
                break;

            default:
                break;
        }
        
    }

}
