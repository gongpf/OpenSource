package com.org.source;

import android.content.Context;
import android.graphics.Color;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.org.source.window.Window;

public class RSSContentWindow extends Window
{
    private ScrollView mContentContainer;
    private TextView mContentView;
    private SyndEntry mSyndEntry;
    
    public RSSContentWindow(Context context)
    {
        super(context);
        init(context);
    }
    
    private void init(Context context)
    {
        mContentContainer = new ScrollView(context);
        mContentContainer.setBackgroundColor(Color.BLACK);
        setContentView(mContentContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mContentView = new TextView(context);
        mContentContainer.addView(mContentView);
        
    }
    
    public void loadData(SyndEntry entry)
    {
        if (null == entry || entry == mSyndEntry)
        {
            return ;
        }
        
        mSyndEntry = entry;
        mContentView.setText(entry.getContents().toString());
    }
}
