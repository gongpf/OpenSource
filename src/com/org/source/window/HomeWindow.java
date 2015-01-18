package com.org.source.window;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.org.source.RSSController;
import com.org.source.widget.menudrawer.MenuDrawer;
import com.org.source.widget.menudrawer.Position;

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
        
        Button mButton = new Button(context);
        mButton.setText("content");
        mButton.setOnClickListener(this);
        
        mHomeWidget.setMenuView(mTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mHomeWidget.setContentView(mButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        mHomeWidget.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        
        setContentView(mHomeWidget, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setWindowSwiper(null);
    }

    private RSSController mRssController = new RSSController(getContext());
    
    @Override
    public void onClick(View v)
    {
        mRssController.openListWindow("http://songshuhui.net/feed");
    }
    

}
