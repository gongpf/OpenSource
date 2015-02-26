package com.org.source.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.org.source.base.ContextManager;
import com.org.source.sm.model.ArticleImage;
import com.org.source.widget.ViewPager.PagerAdapter;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.widget.ViewPager.ViewPager.OnPageChangeListener;

public class NetImageReader extends FrameLayout {

    private ViewPager mViewPager;
    private ImagePageAdapter mPagerAdapter;

    public NetImageReader() {
        super(ContextManager.getContext());
        init();
    }

    private void init() {
        mViewPager = new ViewPager(ContextManager.getContext());
        mPagerAdapter = new ImagePageAdapter();
        mViewPager.setAdapter(mPagerAdapter);
        addView(mViewPager, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        setBackgroundColor(Color.BLACK);
    }

    public void update(List<ArticleImage> list) {
        mPagerAdapter.update(list);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class ImagePageAdapter extends PagerAdapter {

        private Stack<NetImageItem> mRecylerViews = new Stack<NetImageItem>();
        private List<ArticleImage> mItems = new ArrayList<ArticleImage>();

        public void update(List<ArticleImage> list) {
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
        public void destroyItem(ViewGroup container, int position, Object object) {
            NetImageItem view = (NetImageItem) object;
            container.removeView(view);
            view.recyle();
            mRecylerViews.add(view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NetImageItem view = null;
            if (!mRecylerViews.empty()) {
                view = mRecylerViews.pop();
            } else {
                view = new NetImageItem();
            }
            ArticleImage image = mItems.get(position);
            view.setImageUrl(image.getUrl());
            view.setTitle(image.getTitle());
            view.setDescription(image.getDescription());
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    public void registerOnPageChangeListener(OnPageChangeListener listener) {
        mViewPager.registerOnPageChangeListener(listener);
    }
}
