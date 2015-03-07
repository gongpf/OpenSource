package com.org.source.sm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.org.source.R;
import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.sm.SMRequestAsynTask.SMRequestCallBack;
import com.org.source.sm.model.AllChannelJsonResonse;
import com.org.source.sm.model.Channel;
import com.org.source.sm.model.ChannelListResponse;
import com.org.source.sm.model.ChannelList;
import com.org.source.sm.model.DaoHelper;
import com.org.source.widget.ToolBar;
import com.org.source.widget.ViewPager.PagerAdapter;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.widget.ViewPager.ViewPager.OnPageChangeListener;
import com.org.source.widget.viewpagerindicator.TabPageIndicator;

public class SMMainWidget extends LinearLayout
{
    private ToolBar mTabBar;
    private TabPageIndicator mIndicator;
    private SMPageAdapter mPagerAdapter;
    private ViewPager mViewPager;
    
    public SMMainWidget()
    {
        super(ContextManager.getContext());
        init();
    }
    
    private void init()
    {
        setOrientation(LinearLayout.VERTICAL);
        
        mTabBar = new ToolBar();
        LayoutParams toolLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mTabBar, toolLayoutParams);
        mTabBar.setTitle("神马新闻");
        mTabBar.setLeftImage(getResources().getDrawable(R.drawable.titlebar_icon_profile_normal));
        
        mViewPager = new ViewPager(ContextManager.getContext());
        mPagerAdapter = new SMPageAdapter(); 
        mViewPager.setAdapter(mPagerAdapter);

        mIndicator = new TabPageIndicator(ContextManager.getContext());
        mIndicator.setViewPager(mViewPager);
        LayoutParams indicatorLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mIndicator, indicatorLayoutParams);
        int padding = ScreenUtils.dpToPxInt(10); 
        mIndicator.setPadding(padding, padding, padding, padding);
        
        addView(mViewPager);
    }
    
    public void loadData(){
        List<Channel> channels = DaoHelper.getDaoSession().getChannelDao().loadAll();
        update(channels);
    }

    public void requestAllChannel() {
        String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channels/?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";
        new SMRequestAsynTask<ChannelListResponse>(ChannelListResponse.class,
                mCallback).execute(baseUrl);
    }
    
//    private SMRequestCallBack<AllChannelJsonResonse> mCallback = new SMRequestCallBack<AllChannelJsonResonse>() {
//        @Override
//        public void onFinished(AllChannelJsonResonse result) {
//            if (null != result && null != result.getData()) {
//                ChannelList list = result.getData();
//                list.save();
//                update(list.getChannel());
//            }
//        };
//    };

    private SMRequestCallBack<ChannelListResponse> mCallback = new SMRequestCallBack<ChannelListResponse>() {
        @Override
        public void onFinished(ChannelListResponse result) {
            if (null != result && null != result.getData()) {
                ChannelList list = result.getData();
                list.save();
                update(list.getChannel());
            }
        };
    };
    
    public void update(List<Channel> list) {
        List<Channel> newList = new ArrayList<Channel>();
        if (null != list) {
            for (Channel channel : list) {
                if (channel.getIs_subscribed() && channel.getStatus() == 0) {
                    newList.add(channel);
                }
            }
        }
        mPagerAdapter.update(newList);
        mViewPager.setAdapter(mPagerAdapter);
        mIndicator.notifyDataSetChanged();
    }
    
    private class SMPageAdapter extends PagerAdapter {
        private Stack<SMArticalListWidget> mRecylerViews = new Stack<SMArticalListWidget>();
        private List<Channel> mItems = new ArrayList<Channel>();

        @Override
        public CharSequence getPageTitle(int position) {
            return mItems.get(position).getName();
        }

        public void update(List<Channel> list) {
            if (null == list || list.size() < 0) {
                mItems.clear();
            } else {
                mItems = list; 
            }
        }
        
        @Override
        public int getCount() {
            return null == mItems ? 0 : mItems.size();
        }

        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            SMArticalListWidget view = (SMArticalListWidget) object;
            container.removeView(view);
            view.recyle();
            mRecylerViews.add(view);
        }  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {    
            SMArticalListWidget view = null;
            if (!mRecylerViews.empty()) {
                view = mRecylerViews.pop();
            } else {
                view = new SMArticalListWidget();
            }
            Channel channel = mItems.get(position);
            view.setChannel(channel);
            container.addView(view);
            return view; 
        }  

        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {             
            return arg0==arg1; 
        }  
    }

    public void registerOnPageChangeListener(OnPageChangeListener listener) {
        mViewPager.registerOnPageChangeListener(listener);
    }
}
