package com.org.source.sm;

import org.xml.sax.XMLReader;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.sm.model.Article;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;
import com.org.source.window.Window;

public class SMArticleContentWindow extends Window
{
    private ScrollView mContentContainer;
    private TextView mContentView;
    private Article mItem;
    
    public SMArticleContentWindow()
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
    
    public void setData(Article item)
    {
        mItem = item;
    }

    public void updateData()
    {
        loadData(null == mItem ? "" : mItem.getContent());
    }
    
    private void loadData(String result)
    {
        mContentView.setText(TextUtils.isEmpty(result) ? "" : Html.fromHtml(result, new SMImagerGetter(), new SMHandler()));
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
    
    private class SMImagerGetter implements Html.ImageGetter {
        @Override
        public Drawable getDrawable(String source) {
            Log.e("get", source);
            return null;
        }
    }
    
    private class SMHandler implements Html.TagHandler {
        @Override
        public void handleTag(boolean opening, String tag, Editable output,
                XMLReader xmlReader) {
            Log.e("get", tag);
        }
    }
}
