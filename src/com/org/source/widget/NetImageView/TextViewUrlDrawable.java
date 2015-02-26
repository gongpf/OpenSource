package com.org.source.widget.NetImageView;

import java.lang.ref.WeakReference;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.org.source.widget.NetImageView.NetBitmapProxy.UrlBitmapCallback;

public class TextViewUrlDrawable extends BitmapDrawable implements
        UrlBitmapCallback {
    private final WeakReference<TextView> mTextViewRef;

    private Drawable mDrawable;
    private final NetBitmapProxy mUrlBitmap;

    @SuppressWarnings("deprecation")
    public TextViewUrlDrawable(TextView textView, String url) {
        super();
        mTextViewRef = new WeakReference<TextView>(textView);
        mDrawable = new ColorDrawable(Color.YELLOW);
        mUrlBitmap = new NetBitmapProxy(url, this);
    }

    public void setImageUrl(String imageUrl) {
        mUrlBitmap.setImageUrl(imageUrl);
    }

    private void setDrawable(Drawable ndrawable) {
        TextView tx = mTextViewRef.get();

        if (tx == null || null == mDrawable) {
            return;
        }

        mDrawable = ndrawable;
        float fraction = (mDrawable.getIntrinsicHeight() + 0.5f)
                / mDrawable.getIntrinsicWidth();
        int width = tx.getWidth();
        int height = (int) (width * fraction);
        mDrawable.setBounds(0, 0, width, height);
        setBounds(0, 0, width, height);
        tx.setText(tx.getText());
    }

    @Override
    public void draw(Canvas canvas) {
        if (null == mDrawable) {
            return;
        }

        mDrawable.draw(canvas);
    }

    public void setInitImageDrawable(NetBitmapProxy.State state,
            Drawable drawable) {
        mUrlBitmap.setDrawableState(state, drawable);
    }

    @Override
    public void onNotify(Drawable drawable) {
        setDrawable(drawable);
    }
}
