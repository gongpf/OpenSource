package com.org.source.main;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.sm.SMMainWidget;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.widget.menudrawer.MenuDrawer;
import com.org.source.widget.menudrawer.Position;
import com.org.source.window.Window;

public class HomeWindow extends Window 
{
    private MenuDrawer mHomeWidget;
    private SMMainWidget mSmMainWidget;
    
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
        mSmMainWidget = new SMMainWidget();
        
        TextView mTextView = new TextView(ContextManager.getContext());
        mTextView.setText("Menu");
        mHomeWidget.setMenuView(mTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mHomeWidget.setContentView(mSmMainWidget, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mHomeWidget.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        
        mHomeWidget.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener()
        {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y)
            {
                if (v == mSmMainWidget)
                {
                    return !(mPagerPosition == 0 && mPagerOffsetPixels == 0) || dx < 0;
                }
                return false;
            }
        });

        mSmMainWidget.registerOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
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
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
        return false;
    }

    @Override
    public void onWindowAttached()
    {
        mSmMainWidget.requestAllChannel();
    }

    @Override
    public void onWindowDetached()
    {
    }
}
