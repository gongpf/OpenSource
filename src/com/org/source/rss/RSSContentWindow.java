package com.org.source.rss;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.org.source.common.util.ScreenUtils;
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
        mContentContainer.setBackgroundColor(Color.WHITE);
        setContentView(mContentContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mContentView = new TextView(context);
        mContentView.setTextColor(Color.BLACK);
        int leftOrRightPadding = ScreenUtils.dpToPxInt(context, 8);
        int upOrDownPadding = ScreenUtils.dpToPxInt(context, 20);
        mContentView.setPadding(leftOrRightPadding, upOrDownPadding, leftOrRightPadding, upOrDownPadding);
        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(context, 16));
        mContentView.setLineSpacing(ScreenUtils.dpToPx(context, 3), 1.0f);
        
        
        mContentContainer.addView(mContentView);
        
    }
    
    public void loadData(SyndEntry entry)
    {
        if (null == entry || entry == mSyndEntry)
        {
            return ;
        }
        
        mSyndEntry = entry;

        SyndContent description = entry.getDescription();
        String result = ""; 

        List contents = entry.getContents();

        if (null != contents && contents.size() > 0)
        {
            for (Object item : contents)
            {
                SyndContent content = (SyndContent) item;
                result += content.getValue();
            }
        }

        if (TextUtils.isEmpty(result))
        {
            result = null != description ? description.getValue() : "";
        }
        
        if (!TextUtils.isEmpty(result))
        {
            mContentView.setText(Html.fromHtml(result, new RssImageGetter(), null));
        }
    }
    
    public class RssImageGetter implements ImageGetter
    {
        @Override
        public Drawable getDrawable(String source)
        {
            if (TextUtils.isEmpty(source))
            {
                return null;
            }

            return new UrlDrawable(mContentView, source);
        }
    }
}
