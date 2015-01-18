package com.org.source;

import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.org.source.RSSController.EventType;
import com.org.source.RSSController.RSSEvent;
import com.org.source.eventbus.EventBus;
import com.org.source.widget.UrlImageView.UrlImageView;
import com.org.source.window.Window;

public class RSSListWindow extends Window
{
    private ListView mListView;
    private RssAdapter mAdapter;
    
    public RSSListWindow(Context context)
    {
        super(context);
        init(context);
        initItemClickListener();
    }
    
    private void init(Context context)
    {
        mListView = new ListView(context);
        setContentView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mAdapter = new RssAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setBackgroundColor(Color.BLACK);
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
        mAdapter.request(url);
        return this;
    }
    
    public class RssAdapter extends BaseAdapter
    {
        public class RssAsynTask extends AsyncTask<String, Void, SyndFeed>
        {
            @Override
            protected SyndFeed doInBackground(String... params)
            {
                if (params.length <= 0)
                {
                    throw new RuntimeException("Invalid params");
                }

                URLConnection urlConn = null;
                SyndFeed syndFeed = null;
                try
                {
                    urlConn = new URL(params[0]).openConnection();
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
                    update(result.getEntries());
                }
            }
        }

        private List<SyndEntry> mSyndEntries;

        public void request(String url)
        {
            new RssAsynTask().execute(url); 
        }
        
        private void update(List<SyndEntry> list)
        {
            mSyndEntries = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return null != mSyndEntries ? mSyndEntries.size() : 0;
        }

        @Override
        public Object getItem(int position)
        {
            return mSyndEntries.get(position);
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
                convertView = new RSSView(getContext());
            }

            RSSView item = (RSSView) convertView;
            SyndEntry entry = mSyndEntries.get(position);

            String time = null == entry.getUpdatedDate() ? "" : entry.getUpdatedDate().toLocaleString();
            item.setText(entry.getTitle(), time);
            item.setUrl("http://www.baidu.com/img/bd_logo1.png");
            return item;
        }
    }
    
    public class RSSView extends LinearLayout
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
            
            LinearLayout textContainer = new LinearLayout(context);
            textContainer.setOrientation(LinearLayout.VERTICAL);
            
            mTitle = new TextView(context);
            textContainer.addView(mTitle);
            
            mTime = new TextView(context);
            textContainer.addView(mTime);
            
            mImageView = new UrlImageView(context);
            
            addView(textContainer, new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            addView(mImageView, 500, 300);
        }
        
        public void setText(String title, String time)
        {
            mTime.setText(time);
            mTitle.setText(title);
        }
        
        public void setUrl(String imageUrl)
        {
            mImageView.setImageUrl(imageUrl);
        }
    }
}
