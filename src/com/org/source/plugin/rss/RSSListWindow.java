package com.org.source.plugin.rss;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.R.string;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.org.source.activeandroid.ActiveAndroid;
import com.org.source.activeandroid.query.Select;
import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.EventType;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.widget.UrlImageView.UrlImageView;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.org.source.widget.pulltorefresh.library.PullToRefreshListView;
import com.org.source.window.Window;

public class RSSListWindow extends Window
{
	private PullToRefreshListView mListView;
    private RssAdapter mAdapter;
    private String mUrl;
    
    public RSSListWindow(Context context)
    {
        super(context);
        init(context);
        initItemClickListener();
    }
    
    private void init(Context context)
    {
        mListView = new PullToRefreshListView(context);
        setContentView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mAdapter = new RssAdapter();
        ListView listView = mListView.getRefreshableView();
        listView.setAdapter(mAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setDivider(new ColorDrawable(DefaultColor.default_divider_color));
        listView.setDividerHeight(1);
        
        mListView.setOnRefreshListener(new OnRefreshListener<ListView>()
        {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView)
            {
                String label = DateUtils.formatDateTime(ContextManager.getAppContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new RssAsynTask(mUrl, mAdapter, mListView).execute(); 
            }
        });
        
    }
    
    private void initItemClickListener()
    {
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                SyndEntry entry = (SyndEntry)arg0.getAdapter().getItem(arg2);
                RSSEvent event = new RSSEvent();
                event.mEventType = EventType.OPENCONTENTWINDOW;
                event.mObject = entry;
                EventBus.getDefault().post(event);
            }
        });
    }
    
    public RSSListWindow loadUrl(String url)
    {
        mUrl = url;
        new RssAsynTask(mUrl, mAdapter, mListView).execute(); 
        return this;
    }
    
    public static class RssAsynTask extends AsyncTask<Void, Void, SyndFeed>
    {
        private final String mOriginUrl;
        
        private final WeakReference<RssAdapter> mAdapterRf;
        
        private final WeakReference<PullToRefreshListView> mListViewRf;
        
        public RssAsynTask(String originUrl, RssAdapter adapter, PullToRefreshListView listView)
        {
            mOriginUrl = originUrl;
            mAdapterRf = new WeakReference<RSSListWindow.RssAdapter>(adapter);
            mListViewRf = new WeakReference<PullToRefreshListView>(listView);
        }
        
        @Override
        protected SyndFeed doInBackground(Void... params)
        {
            if (TextUtils.isEmpty(mOriginUrl))
            {
                throw new RuntimeException("Invalid origin url");
            }

            URLConnection urlConn = null;
            SyndFeed syndFeed = null;
            try
            {
                urlConn = new URL(mOriginUrl).openConnection();
                urlConn.setConnectTimeout(5000);
                SyndFeedInput input = new SyndFeedInput();
                syndFeed = input.build(new XmlReader(urlConn));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return syndFeed;
        }

        @Override
        protected void onPostExecute(SyndFeed result)
        {
            if (null != result)
            {
                saveToDB(result);
                
                if (null != mAdapterRf.get() && null != mListViewRf.get())
                {
                    mAdapterRf.get().update(mOriginUrl);
                    mListViewRf.get().onRefreshComplete();
                }
            }
        }
        
        private void saveToDB(SyndFeed result)
        {
            if (null != result)
            {
                ActiveAndroid.beginTransaction();
                @SuppressWarnings("unchecked")
                List<SyndEntry> entryList = result.getEntries();
                for (SyndEntry entry : entryList)
                {
                    RSSItem.save(mOriginUrl, entry.getTitle(), entry.getUpdatedDate().toLocaleString(), 
                            HtmlUtil.getUrl(entry));
                }
                ActiveAndroid.setTransactionSuccessful();
                ActiveAndroid.endTransaction();
            }
        }
    }

    public static class RssAdapter extends BaseAdapter
    {
        private List<RSSItem> mItems;
        
        public void update(String url)
        {
            mItems.clear();
            mItems = new Select().from(RSSItem.class).and("originurl = ?", url).execute();
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
        private UrlImageView mImageView;
        
        public RSSView(Context context)
        {
            super(context);
            init(context);
        }
        
        private void init(Context context)
        {
            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            int padding = ScreenUtils.dpToPxInt(context, 15);
            setPadding(padding, padding, padding, padding);
            
            LinearLayout textContainer = new LinearLayout(context);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            
            mTitle = new TextView(context);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(context, 17));
            mTitle.setTextColor(DefaultColor.default_text_color);
            textContainer.addView(mTitle);
            
            mTime = new TextView(context);
            mTime.setTextColor(DefaultColor.default_secondary_text_color);
            mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(context, 12));
            LayoutParams timeLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            timeLayoutParams.topMargin = ScreenUtils.dpToPxInt(context, 10);
            textContainer.addView(mTime, timeLayoutParams);
            
            mImageView = new UrlImageView(context);
            
            addView(textContainer, new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            addView(mImageView, ScreenUtils.dpToPxInt(context, 100), ScreenUtils.dpToPxInt(context, 60));
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
