package com.org.source.widget;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;
import com.org.source.widget.NetImageView.NetImageView;

public class NetImageItem extends FrameLayout{
    
    private NetImageView mUrlImageView;
    private TextView mTitleView;
    private TextView mDescription;
    private ScrollView mDescriptionScrollView;
    private LinearLayout mContainerLayout;

    public NetImageItem() {
        super(ContextManager.getContext());
        init();
    }
    
    private void init(){
        setBackgroundColor(Color.BLACK);
        mUrlImageView = new NetImageView(ContextManager.getContext());
        addView(mUrlImageView);
        
        mContainerLayout = new LinearLayout(ContextManager.getContext());
        mContainerLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams containerLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.dpToPxInt(150));
        containerLayoutParams.gravity = Gravity.BOTTOM;
        containerLayoutParams.leftMargin = containerLayoutParams.rightMargin = ScreenUtils.dpToPxInt(10);
        addView(mContainerLayout, containerLayoutParams);
        
        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTitleView = new TextView(ContextManager.getContext());
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(19));
        mTitleView.setTextColor(Color.WHITE);
        mContainerLayout.addView(mTitleView, titleParams);

        mDescriptionScrollView = new ScrollView(ContextManager.getContext());
        mContainerLayout.addView(mDescriptionScrollView);

        LayoutParams desParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mDescription = new TextView(ContextManager.getContext());
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(14));
        mDescription.setTextColor(Color.WHITE);
        mContainerLayout.addView(mDescription, desParams);
    }
    
    public void setTitle(String title) {
        mTitleView.setText(title);
    }
    
    public void setDescription(String des) {
        mDescription.setText(des);
    }
    
    public void setImageUrl(String url) {
        mUrlImageView.setImageUrl(url);
    }
    
    public void recyle() {
        mUrlImageView.setImageUrl(null);
    }
}
