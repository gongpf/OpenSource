package com.org.source.plugin.rss;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.plugin.rss.model.RSSItem;
import com.org.source.widget.NetImageView.TextViewUrlDrawable;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;
import com.org.source.window.Window;

public class RSSContentWindow extends Window
{
    private ScrollView mContentContainer;
    private TextView mContentView;
    private RSSItem mItem;
    
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
        setInterceptMoveEventListener(mInterceptMoveEventListener);
    }
    
    private OnInterceptMoveEventListener mInterceptMoveEventListener = new OnInterceptMoveEventListener()
    {
        @Override
        public boolean isViewDraggableHorizontally(View v, int dx, int x, int y)
        {
            return false;
        }

        @Override
        public boolean isViewDraggableVertically(View v, int dy, int x, int y)
        {
            boolean ret = false;

            if (v == mContentContainer)
            {
                View chidView = mContentContainer.getChildAt(0);
                int scrollY = mContentContainer.getScrollY();

                ret = dy < 0 && scrollY < (chidView.getHeight() - mContentContainer.getHeight());
                ret |= dy > 0 && scrollY > 0;
                return ret;
            }

            return ret;
        }
    };
    
    public void setData(RSSItem item)
    {
        mItem = item;
    }

    public void updateData()
    {
        loadData(null == mItem ? "" : mItem.text);
    }
    
    private void loadData(String result)
    {
        mContentView.setText(TextUtils.isEmpty(result) ? "" : Html.fromHtml(result, new RssImageGetter(), null));
    }

    @Override
    public void onWindowAttached()
    {
        updateData();
    }

    @Override
    public void onWindowDetached()
    {
        setData(null);
        updateData();
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
