package com.org.source.widget.NetImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.org.source.widget.NetImageView.NetBitmapProxy.UrlBitmapCallback;

public class NetImageView extends ImageView implements UrlBitmapCallback {
    private final NetBitmapProxy mUrlBitmap;

    public NetImageView(Context context) {
        this(context, null);
    }

    public NetImageView(Context context, String imageUrl) {
        super(context);
        mUrlBitmap = new NetBitmapProxy(imageUrl, this);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        throw new RuntimeException("Cann't use this interface");
    }

    @Override
    public void setImageResource(int resId) {
        throw new RuntimeException("Cann't use this interface");
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        throw new RuntimeException("Cann't use this interface");
    }

    private void setDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public void setImageUrl(String imageUrl) {
        mUrlBitmap.setImageUrl(imageUrl);
    }

    public void setDrawableState(NetBitmapProxy.State state, Drawable drawable) {
        mUrlBitmap.setDrawableState(state, drawable);
    }

    @Override
    public void onNotify(Drawable drawable) {
        setDrawable(drawable);
    }
}
