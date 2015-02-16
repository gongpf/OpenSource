package com.org.source.sm;

import java.util.List;

import com.org.source.event.ISystemEventHandler;
import com.org.source.eventbus.EventBus;
import com.org.source.sm.model.Article;
import com.org.source.sm.model.ArticleImage;
import com.org.source.widget.UrlImageViewReader;
import com.org.source.widget.UrlImageWindow;
import com.org.source.window.WindowManager;
import com.org.source.window.WindowManager.WindowEvent;


public class SMController implements ISystemEventHandler
{
    public enum SMEventType {UNKOWN, OPENCONTENTWINDOW, OPENWEBVIEWWINDOW, OPENURLIMAGEWINDOW};
    
    public static class SMEvent
    {
        public SMEventType mEventType = SMEventType.UNKOWN;
        public Object mObject;
    }
    
    public SMController()
    {
        EventBus.getDefault().register(this);
    }
    
    private WebViewWindow mWebViewWindow;
    private WebViewWindow getWebWindow() {
        if (null == mWebViewWindow) {
            mWebViewWindow = new WebViewWindow();
        }
        return mWebViewWindow;
    }

    private UrlImageWindow mUrlImageWindow;
    private UrlImageWindow getUrlImageWindow() {
        if (null == mUrlImageWindow) {
            mUrlImageWindow = new UrlImageWindow();
        }
        return mUrlImageWindow;
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
        WebViewWindow window = getWebWindow();
        window.loadUrl(url);
        windowEvent.mObject = window;
        EventBus.getDefault().post(windowEvent);
    }

    public void openUrlImageWindow(List<ArticleImage> list)
    {
        WindowEvent windowEvent = new WindowEvent();
        windowEvent.mEventType = WindowManager.EventType.PUSHWINDOW;
        UrlImageWindow window = getUrlImageWindow();
        window.update(list);
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
                
            case OPENURLIMAGEWINDOW:
                openUrlImageWindow((List<ArticleImage>) event.mObject);
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
