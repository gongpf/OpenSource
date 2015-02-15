package com.org.source.sm;

import java.util.ArrayList;
import java.util.Date;
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
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.org.source.activeandroid.DatabaseHelper;
import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.eventbus.EventBus;
import com.org.source.plugin.rss.RSSController.RSSEvent;
import com.org.source.plugin.rss.RSSController.RSSEventType;
import com.org.source.plugin.rss.model.RSSItem;
import com.org.source.sm.SMController.SMEvent;
import com.org.source.sm.SMController.SMEventType;
import com.org.source.sm.SMRequestAsynTask.SMRequestCallBack;
import com.org.source.sm.model.Article;
import com.org.source.sm.model.ArticleList;
import com.org.source.sm.model.ArticleThumbnail;
import com.org.source.widget.UrlImageView.UrlImageView;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.org.source.widget.pulltorefresh.library.PullToRefreshListView;

public class SMArticalListWidget extends FrameLayout
{
    private PullToRefreshListView mListView;
    private SMAdapter mAdapter;
    
    public SMArticalListWidget()
    {
        super(ContextManager.getContext());
        init();
    }
    
    private void init()
    {
        mListView = new PullToRefreshListView(ContextManager.getContext());
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        mAdapter = new SMAdapter();
        ListView listView = mListView.getRefreshableView();
        listView.setAdapter(mAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setVerticalFadingEdgeEnabled(false);
        listView.setDivider(new ColorDrawable(DefaultColor.default_divider_color));
        listView.setDividerHeight(1);
        
        mListView.setOnRefreshListener(mOnRefreshListener);
        initItemClickListener();
    }

    private OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>()
    {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView)
        {
            String label = DateUtils.formatDateTime(ContextManager.getAppContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            requestChannel();
        }
    };
    
    private void initItemClickListener()
    {
        mListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                Article item = (Article)arg0.getAdapter().getItem(arg2);
                SMEvent event = new SMEvent();
                event.mEventType = SMEventType.OPENCONTENTWINDOW;
                event.mObject = item;
                EventBus.getDefault().post(event);
            }
        });
    }
    
    @Override
    protected void onAttachedToWindow() {
        requestChannel();
    };
    
    private void requestChannel() {
        String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channel/100?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";
        new SMRequestAsynTask<ChannelJsonResonse>(ChannelJsonResonse.class,
                mCallback).execute(baseUrl);
    }

    
    private SMRequestCallBack<ChannelJsonResonse> mCallback = new SMRequestCallBack<ChannelJsonResonse>()
    {
        @Override
        public void onFinished(ChannelJsonResonse result) {
            mListView.onRefreshComplete();
            if (null != result && null != result.getData()) {
                ArticleList list = result.getData();
                mAdapter.update(list.getArticle());
            }
        };
    };
    
    public static class SMAdapter extends BaseAdapter
    {
        private List<Article> mItems = new ArrayList<Article>();
        
        public void update(List<Article> items)
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
                convertView = new SMArticleView(ContextManager.getContext());
            }

            SMArticleView item = (SMArticleView) convertView;
            Article itemData = mItems.get(position);

            item.setText(itemData.getTitle(), itemData.getPublish_time());
            List<ArticleThumbnail> articleThumbnails = itemData.getThumbnails();
            item.setUrl((articleThumbnails != null && articleThumbnails.size() > 0) ? articleThumbnails.get(0).getUrl() : null);
            return item;
        }
    }
    
    public static class SMArticleView extends LinearLayout
    {
        private TextView mTitle;
        private TextView mTime;
        private UrlImageView mImageView;
        
        public SMArticleView(Context context)
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
            
            mImageView = new UrlImageView(context);
            
            addView(textContainer, new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
            addView(mImageView, ScreenUtils.dpToPxInt(100), ScreenUtils.dpToPxInt(60));
        }
        
        public void setText(String title, String time)
        {
            try {
                Long localTime = Long.valueOf(time);
                mTime.setText(new Date(localTime).toString());
            } catch (Exception e) {
            }
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
