package com.org.source.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.EventType;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.widget.ViewPager.PagerAdapter;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.widget.menudrawer.MenuDrawer;
import com.org.source.widget.menudrawer.Position;
import com.org.source.window.Window;

public class HomeWindow extends Window implements OnClickListener
{
    private MenuDrawer mHomeWidget;
    private ViewPager mViewPager;
    
    private int mPagerPosition;
    private int mPagerOffsetPixels;
    
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
        
        List<View> list = new ArrayList<View>();
        list.add(button1);
        list.add(button2);
        list.add(button3);
        
        PagerAdapter adapter = new MyViewPagerAdapter(list);
        mViewPager = new ViewPager(context);
        mViewPager.setAdapter(adapter);
        
        mHomeWidget.setMenuView(mTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mHomeWidget.setContentView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mHomeWidget.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        
        mHomeWidget.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener()
        {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y)
            {
                if (v == mViewPager)
                {
                    return !(mPagerPosition == 0 && mPagerOffsetPixels == 0) || dx < 0;
                }
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                mPagerPosition = position;
                mPagerOffsetPixels = positionOffsetPixels;
            }
        });
        
        setContentView(mHomeWidget, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setWindowSwiper(null);
    }
    
    public class MyViewPagerAdapter extends PagerAdapter{  
        private List<View> mListViews;  
          
        public MyViewPagerAdapter(List<View> mListViews) {  
            this.mListViews = mListViews;  
        }  
  
        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mListViews.get(position)); 
        }  
  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {    
             container.addView(mListViews.get(position), 0); 
             return mListViews.get(position);  
        }  
  
        @Override  
        public int getCount() {           
            return  mListViews.size(); 
        }  
          
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1; 
        }  
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
                RSSData.save("http://www.36kr.com/feed/", "xx", "xx");
                RSSData.save("http://www.guokr.com/rss/", "xx", "xx");
                RSSData.save("http://rss.cnbeta.com/rss", "xx", "xx");
                RSSData.save("http://www.ifanr.com/feed", "xx", "xx");
                RSSData.save("http://fullrss.net/a/http/feed.iplaysoft.com/", "xx", "xx");
                RSSData.save("http://blog.sina.com.cn/rss/1286528122.xml", "xx", "xx");
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

    @Override
    public void onWindowAttached()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onWindowDetached()
    {
        // TODO Auto-generated method stub
        
    }
}
