package com.org.source.sm;

import android.view.View;

import com.org.source.base.ContextManager;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;
import com.org.source.window.Window;

public class SMMainWindow extends Window
{
    private SMArticalListWidget mListView;
    
    public SMMainWindow()
    {
        super(ContextManager.getContext());
        init();
    }
    
    private void init()
    {
        mListView = new SMArticalListWidget();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setContentView(mListView, params);
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
            return true;
        }
    };
    
    @Override
    public void onWindowAttached()
    {
    }

    @Override
    public void onWindowDetached()
    {
    }
}
