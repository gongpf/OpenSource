package com.org.source.sm;

import com.org.source.event.ISystemEventHandler;
import com.org.source.eventbus.EventBus;
import com.org.source.sm.model.Article;
import com.org.source.window.Window;
import com.org.source.window.WindowManager;
import com.org.source.window.WindowManager.WindowEvent;


public class SMController implements ISystemEventHandler
{
    public enum SMEventType {UNKOWN, OPENMAINWINDOW, OPENCONTENTWINDOW, POPUPWINDOW};
    
    public static class SMEvent
    {
        public SMEventType mEventType = SMEventType.UNKOWN;
        public Object mObject;
    }
    
    private SMMainWindow mMainWindow;
    
    public SMController()
    {
        EventBus.getDefault().register(this);
    }
    
    public Window getMainWindow(){
        if (null == mMainWindow) {
            mMainWindow = new SMMainWindow();
        }
        return mMainWindow;
    }
    
    
    public void openMainWindow()
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        windowEvent.mObject = getMainWindow();
        EventBus.getDefault().post(windowEvent);
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

    public void onEventMainThread(SMEvent event)
    {
        if (null == event)
        {
            return ;
        }
        
        switch(event.mEventType)
        {
            case OPENMAINWINDOW:
                openMainWindow();
                break;
                
            case OPENCONTENTWINDOW:
                openArticleWindow((Article) event.mObject);
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
