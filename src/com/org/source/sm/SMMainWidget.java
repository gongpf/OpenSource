package com.org.source.sm;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.org.source.R;
import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.sm.SMRequestAsynTask.SMRequestCallBack;
import com.org.source.sm.model.AllChannelJsonResonse;
import com.org.source.sm.model.Channel;
import com.org.source.sm.model.ChannelList;
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

    public void requestAllChannel() {
        String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channels/?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";
        new SMRequestAsynTask<AllChannelJsonResonse>(AllChannelJsonResonse.class,
                mCallback).execute(baseUrl);
    }
    
    private SMRequestCallBack<AllChannelJsonResonse> mCallback = new SMRequestCallBack<AllChannelJsonResonse>() {
        @Override
        public void onFinished(AllChannelJsonResonse result) {
            if (null != result && null != result.getData()) {
                ChannelList list = result.getData();
                mPagerAdapter.update(list.getChannel());
            }
        };
    };
    
    private class SMPageAdapter extends PagerAdapter {
        private List<SMArticalListWidget> mItems = new ArrayList<SMArticalListWidget>();

        @Override
        public CharSequence getPageTitle(int position) {
            return mItems.get(position).getTitle();
        }

        public void update(List<Channel> channels) {
            if (null == channels || channels.size() < 0) {
                mItems.clear();
                notifyUi();
                return ;
            }
            
            List<Channel> removeList = new ArrayList<Channel>();
            for (Channel channel : channels) {
                if (1 == channel.getStatus() || !channel.getIs_subscribed()){
                    removeList.add(channel);
                }
            }

            for (Channel channel : removeList) {
                channels.remove(channel);
            }
            
            removeList.clear();
            
            int nChannelCount = channels.size();

            while (mItems.size() < nChannelCount) {
               mItems.add(new SMArticalListWidget()); 
            }

            while (mItems.size() > nChannelCount) {
               mItems.remove(0); 
            }

            for (int i = 0; i < nChannelCount; i++) {
                SMArticalListWidget widget = mItems.get(i);
                Channel channel = channels.get(i);
                widget.setChannel(channel);
            }
            
            notifyUi();
        }
        
        private void notifyUi() {
            notifyDataSetChanged();
            mIndicator.notifyDataSetChanged();
            invalidate();
        }
        @Override
        public int getCount() {
            return null == mItems ? 0 : mItems.size();
        }

        @Override  
        public void destroyItem(ViewGroup container, int position, Object object)   {     
            container.removeView(mItems.get(position)); 
        }  
  
        @Override  
        public Object instantiateItem(ViewGroup container, int position) {    
             container.addView(mItems.get(position), 0); 
             return mItems.get(position);  
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
