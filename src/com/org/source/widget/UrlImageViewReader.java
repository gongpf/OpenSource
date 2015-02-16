package com.org.source.widget;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.org.source.base.ContextManager;
import com.org.source.sm.model.ArticleImage;
import com.org.source.widget.UrlImageView.UrlImageView;
import com.org.source.widget.ViewPager.PagerAdapter;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.widget.ViewPager.ViewPager.OnPageChangeListener;

public class UrlImageViewReader extends FrameLayout{
    
    private ViewPager mViewPager;
    private ImagePageAdapter mPagerAdapter;
    
    public UrlImageViewReader() {
        super(ContextManager.getContext());
        init();
    }
    
    private void init() {
        mViewPager = new ViewPager(ContextManager.getContext());
        mPagerAdapter = new ImagePageAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.BLACK);
    }
    
    public void update(List<ArticleImage> list) {
        mPagerAdapter.update(list);
    }

    private class ImagePageAdapter extends PagerAdapter {
        private List<UrlImageViewExt> mItems = new ArrayList<UrlImageViewExt>();

        public void update(List<ArticleImage> list) {
            if (null == list|| list.size() < 0) {
                list.clear();
                notifyUi();
                return ;
            }
            
            
            int nCount = list.size();

            while (mItems.size() < nCount) {
               mItems.add(new UrlImageViewExt()); 
            }

            while (mItems.size() > nCount) {
               mItems.remove(0); 
            }

            for (int i = 0; i < nCount; i++) {
                UrlImageViewExt widget = mItems.get(i);
                ArticleImage image = list.get(i);
                widget.setImageUrl(image.getUrl());
                widget.setTitle(image.getTitle());
                widget.setDescription(image.getDescription());
            }
            
            notifyUi();
        }
        
        private void notifyUi() {
            notifyDataSetChanged();
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

    public void registerOnPageChangeListener(
            OnPageChangeListener listener) {
        mViewPager.registerOnPageChangeListener(listener);
    }
}
