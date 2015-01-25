package com.org.source.main;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.EventType;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.widget.menudrawer.MenuDrawer;
import com.org.source.widget.menudrawer.Position;
import com.org.source.window.Window;

public class HomeWindow extends Window implements OnClickListener
{
    private MenuDrawer mHomeWidget;
    
    public HomeWindow(Context context)
    {
        super(context);
        init(context);
    }
    
    private void init(Context context)
    {
        mHomeWidget = MenuDrawer.createMenuDrawer(context, Position.LEFT, MenuDrawer.Type.BEHIND);
        
        TextView mTextView = new TextView(context);
        mTextView.setText("menu");
        
        
        Button button1 = new Button(context);
        button1.setText("content1");
        button1.setOnClickListener(this);
        button1.setId(1);

        Button button2 = new Button(context);
        button2.setText("content2");
        button2.setOnClickListener(this);
        button2.setId(2);

        Button button3 = new Button(context);
        button3.setText("content3");
        button3.setOnClickListener(this);
        button3.setId(3);
        
        LinearLayout buttonContaiener = new LinearLayout(context);
        buttonContaiener.setOrientation(LinearLayout.VERTICAL);
        buttonContaiener.addView(button1);
        buttonContaiener.addView(button2);
        buttonContaiener.addView(button3);
        
        mHomeWidget.setMenuView(mTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mHomeWidget.setContentView(buttonContaiener, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mHomeWidget.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        
        setContentView(mHomeWidget, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setWindowSwiper(null);
    }

    @Override
    public void onClick(View v) 
    {
        switch (v.getId())
        {
            case 1:
                RSSEvent event = new RSSEvent();
                event.mEventType = EventType.OPENRSSWINDOW;
                EventBus.getDefault().post(event);
                break;

            case 2:
                RSSData.save("http://coolshell.cn/feed", "xx", "xx");
                RSSData.save("http://www.appinn.com/feed/", "xx", "xx");
                break;

            case 3:
                break;

            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        return false;
    }
}
