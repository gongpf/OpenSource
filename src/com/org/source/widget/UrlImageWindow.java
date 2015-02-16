package com.org.source.widget;

import java.util.List;

import android.view.View;

import com.org.source.base.ContextManager;
import com.org.source.sm.model.ArticleImage;
import com.org.source.widget.ViewPager.ViewPager;
import com.org.source.window.Window;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;

public class UrlImageWindow extends Window {

    private UrlImageViewReader mImageViewReader;
    private int mPagerPosition;
    private int mPagerOffsetPixels;

    public UrlImageWindow() {
        super(ContextManager.getContext());
        mImageViewReader = new UrlImageViewReader();
        setContentView(mImageViewReader, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setInterceptMoveEventListener(mInterceptMoveEventListener);

        mImageViewReader
                .registerOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                            float positionOffset, int positionOffsetPixels) {
                        mPagerPosition = position;
                        mPagerOffsetPixels = positionOffsetPixels;
                    }
                });
    }

    private OnInterceptMoveEventListener mInterceptMoveEventListener = new OnInterceptMoveEventListener() {
        @Override
        public boolean isViewDraggableHorizontally(View v, int dx, int x, int y) {
            if (v == mImageViewReader) {
                return !(mPagerPosition == 0 && mPagerOffsetPixels == 0)
                        || dx < 0;
            }
            return false;
        }

        @Override
        public boolean isViewDraggableVertically(View v, int dy, int x, int y) {
            return true;
        }
    };

    public void update(List<ArticleImage> list) {
        mImageViewReader.update(list);
    }

    @Override
    public void onWindowAttached() {

    }

    @Override
    public void onWindowDetached() {

    }
}
