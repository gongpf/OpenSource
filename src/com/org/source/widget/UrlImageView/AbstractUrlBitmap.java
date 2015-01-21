package com.org.source.widget.UrlImageView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.org.source.common.entity.FailedReason;
import com.org.source.common.service.impl.ImageCache;
import com.org.source.common.service.impl.ImageMemoryCache.OnImageCallbackListener;

public abstract class AbstractUrlBitmap
{
    private static final ImageCache IMAGE_CACHE = new ImageCache(); 
    static
    {
        OnImageCallbackListener imageCallBack = new OnImageCallbackListener()
        {
            // callback function before get image, run on ui thread
            @Override
            public void onPreGet(String imageUrl, Object obj)
            {
                if (null == obj || !(obj instanceof AbstractUrlBitmap))
                {
                    throw new RuntimeException("the view must be UrlBitmap");
                }

                AbstractUrlBitmap bitmapObject = (AbstractUrlBitmap) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl()))
                {
                    bitmapObject.onStartLoading();
                }
            }

            // callback function after get image successfully, run on ui thread
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage, Object obj, boolean isInCache)
            {
                if (null == obj || !(obj instanceof AbstractUrlBitmap) || null == loadedImage)
                {
                    throw new RuntimeException("the view must be UrlBitmap");
                }
                
                AbstractUrlBitmap bitmapObject = (AbstractUrlBitmap) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl()))
                {
                    bitmapObject.onLoadSuccess(loadedImage);
                }
            }

            // callback function after get image failed, run on ui thread
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage, Object obj, FailedReason failedReason)
            {
                if (null == obj || !(obj instanceof AbstractUrlBitmap))
                {
                    throw new RuntimeException("the view must be UrlBitmap");
                }
                
                AbstractUrlBitmap bitmapObject = (AbstractUrlBitmap) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl()))
                {
                    bitmapObject.onLoadError();
                }
            }
            
            @Override
            public void onGetNotInCache(String imageUrl, Object obj)
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

    private Bitmap mBitmap; 
    
    public AbstractUrlBitmap(String imageUrl)
    {
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

    private void onLoadSuccess(Bitmap loadedImage)
    {
        mBitmap = loadedImage;
        onStateChanged(State.SUCCESS);
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
                setImageDrawable(mLoadingDrawable);
                break;
                
            case ERROR:
                setImageDrawable(mErrorDrawable);
                break;
                
            case INIT:
                setImageDrawable(mInitDrawable);
                break;

            case SUCCESS:
                setImageDrawable(mBitmap);
                break;

            default:
                break;
        }
    }

    public abstract void setImageDrawable(Drawable drawable);

    public void setImageDrawable(Bitmap bitmap)
    {
        setImageDrawable(new BitmapDrawable(bitmap));
    }
}
