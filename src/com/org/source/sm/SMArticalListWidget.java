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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.daojson.DaoJson;
import com.org.source.eventbus.EventBus;
import com.org.source.sm.SMController.SMEvent;
import com.org.source.sm.SMController.SMEventType;
import com.org.source.sm.model.Article;
import com.org.source.sm.model.ArticleList;
import com.org.source.sm.model.ArticleListResponse;
import com.org.source.sm.model.ArticleThumbnail;
import com.org.source.sm.model.Channel;
import com.org.source.widget.NetImageView.NetImageView;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase;
import com.org.source.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.org.source.widget.pulltorefresh.library.PullToRefreshListView;

public class SMArticalListWidget extends FrameLayout {
    private PullToRefreshListView mListView;
    private SMAdapter mAdapter;
    private Channel mChannel;

    public SMArticalListWidget() {
        super(ContextManager.getContext());
        init();
    }

    private void init() {
        mListView = new PullToRefreshListView(ContextManager.getContext());
        addView(mListView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        mAdapter = new SMAdapter();
        ListView listView = mListView.getRefreshableView();
        listView.setAdapter(mAdapter);
        listView.setBackgroundColor(Color.WHITE);
        listView.setVerticalFadingEdgeEnabled(false);
        listView.setDivider(new ColorDrawable(
                DefaultColor.default_divider_color));
        listView.setDividerHeight(1);

        mListView.setOnRefreshListener(mOnRefreshListener);
        initItemClickListener();
    }

    private OnRefreshListener<ListView> mOnRefreshListener = new OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            DaoJson.test();
            String label = DateUtils.formatDateTime(
                    ContextManager.getAppContext(), System.currentTimeMillis(),
                    DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            requestChannel();
        }
    };

    private void initItemClickListener() {
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Article item = (Article) arg0.getAdapter().getItem(arg2);
                int type = item.getContent_type();
                if (1 == type) {
                    SMEvent event = new SMEvent();
                    event.mEventType = SMEventType.OPENURLIMAGEWINDOW;
                    event.mObject = item.getImage();
                    EventBus.getDefault().post(event);
                } else {
                    SMEvent event = new SMEvent();
                    event.mEventType = SMEventType.OPENWEBVIEWWINDOW;
                    event.mObject = item.getUrl();
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    public void setChannel(Channel channel) {
        mChannel = channel;
        List<Article> articles = mChannel.getArticles();
        mAdapter.update(articles);
        requestChannel();
    }
    
    private void requestChannel() {
        if (null != mChannel && null != mChannel.getId()) {
            new SMRequestChannleAsynTask(mChannel.getId()).execute();
        }
    }

    private class SMRequestChannleAsynTask extends SMRequestAsynTask<ArticleListResponse> {
        private Long mChannelId = null;
        public SMRequestChannleAsynTask(Long channelId)
        {
            super(ArticleListResponse.class, null);
            mChannelId = channelId;
        }
        
        @Override
        protected ArticleListResponse doInBackground(String... params)
        {
            String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channel/@?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";
            ArticleListResponse result = super.doInBackground(baseUrl.replaceFirst("@", mChannelId.toString()));
            if (null != result && null != result.getData()) {
                ArticleList list = result.getData();
                list.save();
            }
            return result;
        }
        
        @Override
        protected void onPostExecute(ArticleListResponse result)
        {
            mListView.onRefreshComplete();
            
            if (null != result && null != result.getData()
                    && null != mChannel && mChannel.getId().equals(mChannelId))
            {
                mAdapter.update(result.getData().getArticle());
            }
        }
    }
    
    public void recyle() {
        mChannel = null;
        mAdapter.update(null);
    }

    public static class SMAdapter extends BaseAdapter {
        private List<Article> mItems = new ArrayList<Article>();

        private static final int AETICLE_TYPE = 0;
        private static final int IMAGE_TYPE = 1;

        public void update(List<Article> items) {
            mItems = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return null != mItems ? mItems.size() : 0;
        }

        @Override
        public int getItemViewType(int position) {
            int type = mItems.get(position).getContent_type();
            int result = AETICLE_TYPE;

            switch (type) {
            case 1:
                result = IMAGE_TYPE;
                break;

            default:
                break;
            }

            return result;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (null == convertView) {
                switch (type) {
                case IMAGE_TYPE:
                    convertView = new SMImageView(ContextManager.getContext());
                    break;

                default:
                    convertView = new SMArticleView(ContextManager.getContext());
                    break;
                }
            }

            ISMArticleView item = (ISMArticleView) convertView;
            Article itemData = mItems.get(position);

            item.setText(itemData.getTitle(), itemData.getPublish_time());
            List<ArticleThumbnail> articleThumbnails = itemData.getThumbnails();
            ArticleThumbnail articleThumbnail = (articleThumbnails != null && articleThumbnails
                    .size() > 0) ? articleThumbnails.get(0) : null;
            if (null == articleThumbnail) {
                item.setUrl(null);
            } else {
                item.setUrl(articleThumbnail.getUrl());
                if (0 != articleThumbnail.getHeight()
                        && 0 != articleThumbnail.getWidth()) {
                    int width = parent.getWidth();
                    int height = (int) (width * articleThumbnail.getHeight() / (float) articleThumbnail
                            .getWidth());
                    item.setImageViewSize(width, height);
                }
            }

            return (View)item;
        }
    }

    public static interface ISMArticleView {
        public void setText(String title, String time);
        public void setUrl(String imageUrl);
        public void setImageViewSize(int width, int height);
    }

    public static class SMArticleView extends LinearLayout implements ISMArticleView{
        private TextView mTitle;
        private TextView mTime;
        private NetImageView mImageView;

        public SMArticleView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            int padding = ScreenUtils.dpToPxInt(15);
            setPadding(padding, padding, padding, padding);

            LinearLayout textContainer = new LinearLayout(context);
            textContainer.setOrientation(LinearLayout.VERTICAL);

            mTitle = new TextView(context);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ScreenUtils.dpToPxInt(17));
            mTitle.setTextColor(DefaultColor.default_text_color);
            textContainer.addView(mTitle);

            mTime = new TextView(context);
            mTime.setTextColor(DefaultColor.default_secondary_text_color);
            mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ScreenUtils.dpToPxInt(12));
            LayoutParams timeLayoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            timeLayoutParams.topMargin = ScreenUtils.dpToPxInt(10);
            textContainer.addView(mTime, timeLayoutParams);

            mImageView = new NetImageView(context);
            mImageView.setScaleType(ScaleType.CENTER_CROP);

            addView(textContainer, new LinearLayout.LayoutParams(0,
                    LayoutParams.WRAP_CONTENT, 1));
            addView(mImageView, ScreenUtils.dpToPxInt(100),
                    ScreenUtils.dpToPxInt(60));
        }

        @Override
        public void setText(String title, String time) {
            try {
                Long localTime = Long.valueOf(time);
                mTime.setText(new Date(localTime).toLocaleString());
            } catch (Exception e) {
            }
            mTitle.setText(title);
        }

        @Override
        public void setUrl(String imageUrl) {
            if (TextUtils.isEmpty(imageUrl)) {
                mImageView.setVisibility(GONE);
            } else {
                mImageView.setVisibility(VISIBLE);
                mImageView.setImageUrl(imageUrl);
            }
        }

        @Override
        public void setImageViewSize(int width, int height) {
            // TODO Auto-generated method stub
            
        }
    }

    public static class SMImageView extends LinearLayout implements ISMArticleView{
        private TextView mTitle;
        private TextView mTime;
        private NetImageView mImageView;

        public SMImageView(Context context) {
            super(context);
            init(context);
        }

        private void init(Context context) {
            setOrientation(LinearLayout.VERTICAL);
            int padding = ScreenUtils.dpToPxInt(15);
            setPadding(padding, padding, padding, padding);

            mTitle = new TextView(context);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ScreenUtils.dpToPxInt(17));
            mTitle.setTextColor(DefaultColor.default_text_color);
            addView(mTitle);

            mImageView = new NetImageView(context);
            mImageView.setScaleType(ScaleType.CENTER_CROP);
            LayoutParams imageLayoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(250));
            imageLayoutParams.topMargin = ScreenUtils.dpToPxInt(10);
            addView(mImageView, imageLayoutParams);

            mTime = new TextView(context);
            mTime.setTextColor(DefaultColor.default_secondary_text_color);
            mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    ScreenUtils.dpToPxInt(12));
            LayoutParams timeLayoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            timeLayoutParams.topMargin = ScreenUtils.dpToPxInt(10);
            addView(mTime, timeLayoutParams);
        }

        @Override
        public void setText(String title, String time) {
            try {
                Long localTime = Long.valueOf(time);
                mTime.setText(new Date(localTime).toLocaleString());
            } catch (Exception e) {
            }
            mTitle.setText(title);
        }

        @Override
        public void setUrl(String imageUrl) {
            if (TextUtils.isEmpty(imageUrl)) {
                mImageView.setVisibility(GONE);
            } else {
                mImageView.setVisibility(VISIBLE);
                mImageView.setImageUrl(imageUrl);
            }
        }
        
        public void setImageViewSize(int width, int height) {
            LayoutParams params = (LayoutParams) mImageView.getLayoutParams();
            if (null == params) {
                params = new LayoutParams(width, height);
            }else {
                params.width = width;
                params.height = height;
            }
            mImageView.setLayoutParams(params);
        }
    }
}
