package com.org.source.plugin.rss;

import com.org.source.event.ISystemEventHandler;
import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.plugin.rss.model.RSSItem;
import com.org.source.window.WindowManager;
import com.org.source.window.WindowManager.WindowEvent;


public class RSSController implements ISystemEventHandler
{
    public enum RSSEventType {UNKOWN, OPENRSSWINDOW, OPENLISTWINDOW, OPENCONTENTWINDOW, POPUPWINDOW};
    
    public static class RSSEvent
    {
        public RSSEventType mEventType = RSSEventType.UNKOWN;
        public Object mObject;
    }
    
    private RSSMainWindow mRSSWindow;
    private RSSListWindow mListWindow;
    private RSSContentWindow mContentWindow;
    
    public RSSController()
    {
        EventBus.getDefault().register(this);
    }
    
    private RSSListWindow getListWindow()
    {
        if (null == mListWindow)
        {
            mListWindow = new RSSListWindow();
        }
        
        return mListWindow;
    }
    
    private RSSContentWindow getContentWindow()
    {
        if (null == mContentWindow)
        {
            mContentWindow = new RSSContentWindow();
        }
        
        return mContentWindow;
    }
    
    private RSSMainWindow getRSSWindow()
    {
        if (null == mRSSWindow)
        {
            mRSSWindow = new RSSMainWindow();
        }
        
        return mRSSWindow;
    }
    
    public void openMainWindow()
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        windowEvent.mObject = getRSSWindow();
        EventBus.getDefault().post(windowEvent);
    }

    public void openListWindow(RSSData data)
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        windowEvent.mObject = getListWindow();
        getListWindow().update(data);
        EventBus.getDefault().post(windowEvent);
    }
    
    public void openContentWindow(RSSItem data)
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        getContentWindow().setData(data);
        windowEvent.mObject = getContentWindow();
        EventBus.getDefault().post(windowEvent);
    }
    
    public void onEventMainThread(RSSEvent event)
    {
        if (null == event)
        {
            return ;
        }
        
        switch(event.mEventType)
        {
            case OPENLISTWINDOW:
                openListWindow((RSSData)event.mObject);
                break;
                
            case OPENRSSWINDOW:
                openMainWindow();
                break;
                
            case OPENCONTENTWINDOW:
                openContentWindow((RSSItem)event.mObject);
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
