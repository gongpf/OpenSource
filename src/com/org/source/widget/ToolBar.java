package com.org.source.widget;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.source.base.ContextManager;
import com.org.source.common.util.ScreenUtils;

public class ToolBar extends FrameLayout {
    private ImageView mLeftButton;
    private TextView mTitleView;
    private ImageView mRightButton;

    public ToolBar() {
        super(ContextManager.getContext());
        init();
    }

    private void init() {
        mLeftButton = new ImageView(ContextManager.getContext());
        mRightButton = new ImageView(ContextManager.getContext());
        mTitleView = new TextView(ContextManager.getContext());
        
        LayoutParams leftParams = new LayoutParams(ScreenUtils.dpToPxInt(32), ScreenUtils.dpToPxInt(32));
        leftParams.leftMargin = ScreenUtils.dpToPxInt(10);
        leftParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        addView(mLeftButton, leftParams);

        LayoutParams rightParams = new LayoutParams(ScreenUtils.dpToPxInt(32), ScreenUtils.dpToPxInt(32));
        rightParams.rightMargin = ScreenUtils.dpToPxInt(10);
        rightParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        addView(mRightButton, rightParams);

        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        addView(mTitleView, titleParams);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dpToPxInt(20));
        mTitleView.setTextColor(Color.WHITE);
        
        setBackgroundColor(Color.RED);
        setPadding(0, ScreenUtils.dpToPxInt(7), 0, ScreenUtils.dpToPxInt(7));
    }

    public void setTitle(String text) {
        mTitleView.setText(text);
    }
    
    public void setLeftImage(Drawable drawable) {
        mLeftButton.setImageDrawable(drawable);
    }

    public void setRightImage(Drawable drawable) {
        mRightButton.setImageDrawable(drawable);
    }
}
