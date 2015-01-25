package com.org.source.plugin.rss;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.ScrollView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.plugin.rss.model.RSSItem;
import com.org.source.widget.UrlImageView.TextViewUrlDrawable;
import com.org.source.window.Window;

public class RSSContentWindow extends Window
{
    private ScrollView mContentContainer;
    private TextView mContentView;
    
    public RSSContentWindow()
    {
        super(ContextManager.getContext());
        init();
    }
    
    private void init()
    {
        mContentContainer = new ScrollView(ContextManager.getContext());
        mContentContainer.setBackgroundColor(Color.WHITE);
        setContentView(mContentContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        int leftOrRightPadding = ScreenUtils.dpToPxInt(8);
        int upOrDownPadding = ScreenUtils.dpToPxInt(20);
        mContentContainer.setPadding(leftOrRightPadding, upOrDownPadding, leftOrRightPadding, upOrDownPadding);

        mContentView = new TextView(ContextManager.getContext());
        mContentView.setTextColor(Color.BLACK);

        mContentView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(16));
        mContentView.setLineSpacing(ScreenUtils.dpToPx(3), 1.0f);
        
        
        mContentContainer.addView(mContentView);
        
    }
    
    public void updateData(RSSItem item)
    {
        loadData(item.text);
    }
    
    public void loadData(String result)
    {
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

            return new TextViewUrlDrawable(mContentView, source);
        }
    }
}
