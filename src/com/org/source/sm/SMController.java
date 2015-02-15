package com.org.source.sm;

import com.org.source.event.ISystemEventHandler;
import com.org.source.eventbus.EventBus;
import com.org.source.sm.model.Article;
import com.org.source.window.WindowManager;
import com.org.source.window.WindowManager.WindowEvent;


public class SMController implements ISystemEventHandler
{
    public enum SMEventType {UNKOWN, OPENCONTENTWINDOW, OPENWEBVIEWWINDOW};
    
    public static class SMEvent
    {
        public SMEventType mEventType = SMEventType.UNKOWN;
        public Object mObject;
    }
    
    public SMController()
    {
        EventBus.getDefault().register(this);
    }
    
    public void openArticleWindow(Article article)
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        SMArticleContentWindow window = new SMArticleContentWindow();
        window.setData(article);
        windowEvent.mObject = window;
        EventBus.getDefault().post(windowEvent);
    }
    
    public void openWebWindow(String url)
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        WebViewWindow window = new WebViewWindow();
        window.loadUrl(url);
        windowEvent.mObject = window;
        EventBus.getDefault().post(windowEvent);
    }

    public void onEventMainThread(SMEvent event)
    {
        if (null == event)
        {
            return ;
        }
        
        switch(event.mEventType)
        {
            case OPENCONTENTWINDOW:
                openArticleWindow((Article) event.mObject);
                break;
                
            case OPENWEBVIEWWINDOW:
                openWebWindow((String) event.mObject);
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
