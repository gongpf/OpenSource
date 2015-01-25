package com.org.source.plugin.rss;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.source.activeandroid.query.Select;
import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.EventType;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.widget.UrlImageView.UrlImageView;
import com.org.source.window.Window;

public class RSSMainWindow extends Window
{
    private GridView mListView;
    private RssAdapter mAdapter;
    
    public RSSMainWindow()
    {
        super(ContextManager.getContext());
        init();
        initItemClickListener();
    }
    
    private void init()
    {
        mListView = new GridView(ContextManager.getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.topMargin = params.bottomMargin = params.leftMargin = params.rightMargin = ScreenUtils.dpToPxInt(10);
        setContentView(mListView, params);
        
        mAdapter = new RssAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setBackgroundColor(Color.WHITE);
        mListView.setNumColumns(2);
        mListView.setHorizontalSpacing(ScreenUtils.dpToPxInt(10));
        mListView.setVerticalSpacing(ScreenUtils.dpToPxInt(10));
    }
    
    private void initItemClickListener()
    {
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                RSSData data = (RSSData)arg0.getAdapter().getItem(arg2);
                RSSEvent event = new RSSEvent();
                event.mEventType = EventType.OPENLISTWINDOW;
                event.mObject = data;
                EventBus.getDefault().post(event);
            }
        });
    }
    
    public void refresh()
    {
        mAdapter.refresh();
    }

    public static class RssAdapter extends BaseAdapter
    {
        private List<RSSData> mItems = new ArrayList<RSSData>();
        
        public RssAdapter()
        {
            update();
        }
        
        public void update()
        {
            mItems.clear();
            mItems = new Select().from(RSSData.class).execute();
            notifyDataSetChanged();
        }
        
        private RssCallback mCallback = new RssCallback()
        {
            @Override
            public void onFinished(boolean success)
            {
                if (success)
                {
                    update();
                }
            }
        };
        
        public void refresh()
        {
            for (RSSData itemData : mItems)
            {
                new RssAsynTask(itemData.url, mCallback).execute();
            }
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
            RSSData itemData = mItems.get(position);

            item.setText(itemData.title);
            item.setImageUrl(itemData.imageUrl);
            return item;
        }
    }
    
    public static class RSSView extends LinearLayout
    {
        private UrlImageView mImageView;
        private TextView mTitle;
        
        public RSSView(Context context)
        {
            super(context);
            init(context);
        }
        
        private void init(Context context)
        {
            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER);
            int padding = ScreenUtils.dpToPxInt(10);
            setPadding(padding, padding, padding, padding);
            
            mImageView = new UrlImageView(context);
            addView(mImageView, ScreenUtils.dpToPxInt(150), ScreenUtils.dpToPxInt(90));
            
            mTitle = new TextView(context);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(15));
            mTitle.setTextColor(DefaultColor.default_text_color);
            mTitle.setSingleLine();
            LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            titleLayoutParams.topMargin = ScreenUtils.dpToPxInt(10);
            addView(mTitle, titleLayoutParams);
            
            setBackgroundColor(0xaaefefef);
        }
        
        public void setText(String title)
        {
            mTitle.setText(title);
        }
        
        public void setImageUrl(String url)
        {
            mImageView.setImageUrl(url);
        }
    }
}
