package com.org.source.widget.UrlImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.org.source.common.entity.FailedReason;
import com.org.source.common.service.impl.ImageCache;
import com.org.source.common.service.impl.ImageMemoryCache.OnImageCallbackListener;

public class UrlImageView extends ImageView
{
    private static final ImageCache IMAGE_CACHE = new ImageCache(); 
    static
    {
        OnImageCallbackListener imageCallBack = new OnImageCallbackListener()
        {
            // callback function before get image, run on ui thread
            @Override
            public void onPreGet(String imageUrl, View view)
            {
                if (null == view || !(view instanceof UrlImageView))
                {
                    throw new RuntimeException("the view must be UrlImageView");
                }

                UrlImageView imageView = (UrlImageView) view;

                if (imageUrl.equals(imageView.getImageUrl()))
                {
                    imageView.onStartLoading();
                }
            }

            // callback function after get image successfully, run on ui thread
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage, View view, boolean isInCache)
            {
                if (null == view || !(view instanceof UrlImageView) || null == loadedImage)
                {
                    throw new RuntimeException("the view must be UrlImageView");
                }
                
                UrlImageView imageView = (UrlImageView) view;

                if (imageUrl.equals(imageView.getImageUrl()))
                {
                    imageView.setImageBitmap(loadedImage);
                }
            }

            // callback function after get image failed, run on ui thread
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage, View view, FailedReason failedReason)
            {
                if (null == view || !(view instanceof UrlImageView))
                {
                    throw new RuntimeException("the view must be UrlImageView");
                }
                
                UrlImageView imageView = (UrlImageView) view;

                if (imageUrl.equals(imageView.getImageUrl()))
                {
                    imageView.onLoadError();
                }
            }
            
            @Override
            public void onGetNotInCache(String imageUrl, View view)
            {
            }
        };
        
        IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
    }

    private String mImageUrl;

    private State mState = State.INIT;
    private enum State {INIT, LOADING, SUCCESS, ERROR};
    
    private Drawable mErrorDrawable = new ColorDrawable(Color.GREEN); 
    private Drawable mInitDrawable = new ColorDrawable(Color.YELLOW); 
    private Drawable mLoadingDrawable = new ColorDrawable(Color.BLUE); 
    
    public UrlImageView(Context context)
    {
        this(context, null);
    }

    public UrlImageView(Context context, String imageUrl)
    {
        super(context);
        setImageUrl(imageUrl);
    }
    
    public void setImageUrl(String imageUrl)
    {
        if (TextUtils.isEmpty(imageUrl))
        {
            mImageUrl = imageUrl;
            onStateChanged(State.INIT);
        }
        else if (!imageUrl.equals(mImageUrl))
        {
            mImageUrl = imageUrl;
            IMAGE_CACHE.get(mImageUrl, this);
        }
    }
    
    public String getImageUrl()
    {
        return mImageUrl;
    }
    
    private void onStartLoading()
    {
        onStateChanged(State.LOADING);
    }
    
    @Override
    public void setImageBitmap(Bitmap bm)
    {
        setImageDrawable(new BitmapDrawable(getContext().getResources(), bm));
    }

    @Override
    public void setImageResource(int resId)
    {
        throw new RuntimeException("Cann't use this interface");
    }
    
    @Override
    public void setImageDrawable(Drawable drawable)
    {
        setSuperImageDrawable(drawable);
        onStateChanged(State.SUCCESS);
    }
    
    private void setSuperImageDrawable(Drawable drawable)
    {
        super.setImageDrawable(drawable);
    }
    
    public void setErrorImageDrawable(Drawable drawable)
    {
        mErrorDrawable = drawable;
        refresh();
    }

    public void setLoadingImageDrawable(Drawable drawable)
    {
        mLoadingDrawable = drawable;
        refresh();
    }

    public void setInitImageDrawable(Drawable drawable)
    {
        mInitDrawable = drawable;
        refresh();
    }
    
    private void onLoadError()
    {
        onStateChanged(State.ERROR);
    }
    
    private void onStateChanged(State state)
    {
        mState = state;
        refresh();
    }
    
    private void refresh()
    {
        switch (mState)
        {
            case LOADING:
                setSuperImageDrawable(mLoadingDrawable);
                break;
                
            case ERROR:
                setSuperImageDrawable(mErrorDrawable);
                break;
                
            case INIT:
                setSuperImageDrawable(mInitDrawable);
                break;

            case SUCCESS:
                break;

            default:
                break;
        }
    }
}
