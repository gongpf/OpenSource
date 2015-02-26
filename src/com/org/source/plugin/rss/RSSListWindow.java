package com.org.source.plugin.rss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.RSSEventType;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.plugin.rss.model.RSSItem;
import com.org.source.widget.NetImageView.NetImageView;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.org.source.widget.pulltorefresh.library.PullToRefreshListView;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;
import com.org.source.window.Window;

public class RSSListWindow extends Window
{
    private PullToRefreshListView mListView;
    private RssAdapter mAdapter;
    private RSSData mData;
    
    public RSSListWindow()
    {
        super(ContextManager.getContext());
        init();
        initItemClickListener();
    }
    
    private void init()
    {
        mListView = new PullToRefreshListView(ContextManager.getContext());
        setContentView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mAdapter = new RssAdapter();
        ListView listView = mListView.getRefreshableView();
        listView.setAdapter(mAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(DefaultColor.default_divider_color));
        listView.setDividerHeight(1);
        
        mListView.setOnRefreshListener(mOnRefreshListener);
        setInterceptMoveEventListener(mInterceptMoveEventListener);
    }

    private OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>()
    {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView)
        {
            String label = DateUtils.formatDateTime(ContextManager.getAppContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            new RssAsynTask(mData.url, mCallback).execute();
        }
    };

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
    
    private RssCallback mCallback = new RssCallback()
    {
        @Override
        public void onFinished(boolean success) 
        {
            if (success)
            {
                update();
            }

            mListView.onRefreshComplete();
        };
    };
    
    private void initItemClickListener()
    {
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                RSSItem item = (RSSItem)arg0.getAdapter().getItem(arg2);
                RSSEvent event = new RSSEvent();
                event.mEventType = RSSEventType.OPENCONTENTWINDOW;
                event.mObject = item;
                EventBus.getDefault().post(event);
            }
        });
    }
    
    public RSSListWindow update(RSSData data)
    {
        mData = data;
        return update();
    }
    
    public RSSListWindow update()
    {
        mAdapter.update(null == mData ? null : mData.items());
        return this;
    }

    @Override
    public void onWindowAttached()
    {
    }

    @Override
    public void onWindowDetached()
    {
    }
    
    public static class RssAdapter extends BaseAdapter
    {
        private List<RSSItem> mItems = new ArrayList<RSSItem>();
        
        public void update(List<RSSItem> items)
        {
            mItems.clear();
            mItems = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return null != mItems ? mItems.size() : 0;
        }

        @Override
        public Object getItem(int position)
        {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (null == convertView)
            {
                convertView = new RSSView(ContextManager.getContext());
            }

            RSSView item = (RSSView) convertView;
            RSSItem itemData = mItems.get(position);

            item.setText(itemData.description, itemData.updateTime);
            item.setUrl(itemData.imageUrl);
            return item;
        }
    }
    
    public static class RSSView extends LinearLayout
    {
        private TextView mTitle;
        private TextView mTime;
        private NetImageView mImageView;
        
        public RSSView(Context context)
        {
            super(context);
            init(context);
        }
        
        private void init(Context context)
        {
            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            int padding = ScreenUtils.dpToPxInt(15);
            setPadding(padding, padding, padding, padding);
            
            LinearLayout textContainer = new LinearLayout(context);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            
            mTitle = new TextView(context);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(17));
            mTitle.setTextColor(DefaultColor.default_text_color);
            textContainer.addView(mTitle);
            
            mTime = new TextView(context);
            mTime.setTextColor(DefaultColor.default_secondary_text_color);
            mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(12));
            LayoutParams timeLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            timeLayoutParams.topMargin = ScreenUtils.dpToPxInt(10);
            textContainer.addView(mTime, timeLayoutParams);
            
            mImageView = new NetImageView(context);
            
            addView(textContainer, new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            addView(mImageView, ScreenUtils.dpToPxInt(100), ScreenUtils.dpToPxInt(60));
        }
        
        public void setText(String title, String time)
        {
            mTime.setText(time);
            mTitle.setText(title);
        }
        
        public void setUrl(String imageUrl)
        {
            if (TextUtils.isEmpty(imageUrl))
            {
                mImageView.setVisibility(GONE);
            }
            else 
            {
                mImageView.setVisibility(VISIBLE);
                mImageView.setImageUrl(imageUrl);
            }
        }
    }
}
