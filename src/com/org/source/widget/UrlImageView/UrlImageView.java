package com.org.source.widget.UrlImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;


public class UrlImageView extends ImageView 
{
    private final AbstractUrlBitmap mUrlBitmap;

    private class UrlBitmapImpl extends AbstractUrlBitmap
    {
        public UrlBitmapImpl(String url)
        {
            super(url);
        }

        @Override
        public void setImageDrawable(Drawable drawable) 
        {
            setDrawable(drawable);
        };
    };

    public UrlImageView(Context context)
    {
        this(context, null);
    }

    public UrlImageView(Context context, String imageUrl)
    {
        super(context);
        mUrlBitmap = new UrlBitmapImpl(imageUrl);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm)
    {
        throw new RuntimeException("Cann't use this interface");
    }

    @Override
    public void setImageResource(int resId)
    {
        throw new RuntimeException("Cann't use this interface");
    }

    @Override
    public void setImageDrawable(Drawable drawable)
    {
        throw new RuntimeException("Cann't use this interface");
    }
    
    private void setDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
    }
    
    public void setImageUrl(String imageUrl)
    {
        mUrlBitmap.setImageUrl(imageUrl);
    }
    
    public void setErrorImageDrawable(Drawable drawable)
    {
        mUrlBitmap.setErrorImageDrawable(drawable);
    }

    public void setLoadingImageDrawable(Drawable drawable)
    {
        mUrlBitmap.setLoadingImageDrawable(drawable);
    }

    public void setInitImageDrawable(Drawable drawable)
    {
        mUrlBitmap.setInitImageDrawable(drawable);
    }
}
