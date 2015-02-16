package com.org.source.widget.UrlImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.org.source.common.entity.FailedReason;
import com.org.source.common.service.impl.ImageCache;
import com.org.source.common.service.impl.ImageMemoryCache.OnImageCallbackListener;

public class UrlBitmapProxy {
    private static final ImageCache IMAGE_CACHE = new ImageCache(12, 20);
    static {
        OnImageCallbackListener imageCallBack = new OnImageCallbackListener() {
            // callback function before get image, run on ui thread
            @Override
            public void onPreGet(String imageUrl, Object obj) {
                if (null == obj || !(obj instanceof UrlBitmapProxy)) {
                    throw new RuntimeException("the view must be UrlBitmap");
                }

                UrlBitmapProxy bitmapObject = (UrlBitmapProxy) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl())) {
                    bitmapObject.onStartLoading();
                }
            }

            // callback function after get image successfully, run on ui thread
            @Override
            public void onGetSuccess(String imageUrl, Bitmap loadedImage,
                    Object obj, boolean isInCache) {
                if (null == obj || !(obj instanceof UrlBitmapProxy)
                        || null == loadedImage) {
                    throw new RuntimeException("the view must be UrlBitmap");
                }

                UrlBitmapProxy bitmapObject = (UrlBitmapProxy) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl())) {
                    bitmapObject.onLoadSuccess(loadedImage);
                }
            }

            // callback function after get image failed, run on ui thread
            @Override
            public void onGetFailed(String imageUrl, Bitmap loadedImage,
                    Object obj, FailedReason failedReason) {
                if (null == obj || !(obj instanceof UrlBitmapProxy)) {
                    throw new RuntimeException("the view must be UrlBitmap");
                }

                UrlBitmapProxy bitmapObject = (UrlBitmapProxy) obj;

                if (imageUrl.equals(bitmapObject.getImageUrl())) {
                    bitmapObject.onLoadError();
                }
            }

            @Override
            public void onGetNotInCache(String imageUrl, Object obj) {
            }
        };

        IMAGE_CACHE.setOnImageCallbackListener(imageCallBack);
    }

    private String mImageUrl;
    private State mState = State.INIT;

    public enum State {
        INIT, LOADING, SUCCESS, ERROR
    };

    private Map<State, Drawable> mDrawableStateList = new HashMap<State, Drawable>();
    
    public static interface UrlBitmapCallback {
        public void onNotify(Drawable drawable);
    }
    
    private final WeakReference<UrlBitmapCallback> mCallbackRf;

    public UrlBitmapProxy(String imageUrl, UrlBitmapCallback callback) {
        mCallbackRf = new WeakReference<UrlBitmapCallback>(callback); 
        setDrawableState(State.INIT, new ColorDrawable(Color.GRAY));
        setDrawableState(State.LOADING, new ColorDrawable(Color.GRAY));
        setDrawableState(State.ERROR, new ColorDrawable(Color.YELLOW));
        setImageUrl(imageUrl);
    }

    public void setImageUrl(String imageUrl) {

        if (TextUtils.isEmpty(imageUrl)) {
            mImageUrl = imageUrl;
            onStateChanged(State.INIT);
            return;
        }

        boolean equal = imageUrl.equals(mImageUrl);

        switch (mState) {
        case INIT:
        case ERROR:
            mImageUrl = imageUrl;
            IMAGE_CACHE.get(mImageUrl, this);
            break;

        case SUCCESS:
        case LOADING: {
            if (!equal) {
                mImageUrl = imageUrl;
                IMAGE_CACHE.get(mImageUrl, this);
            }
        }
            break;

        default:
            break;
        }
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    private void onStartLoading() {
        onStateChanged(State.LOADING);
    }

    @SuppressWarnings("deprecation")
    private void onLoadSuccess(Bitmap loadedImage) {
        onStateChanged(State.SUCCESS);
        setDrawableState(State.SUCCESS, new BitmapDrawable(loadedImage));
    }

    public void setDrawableState(State state, Drawable drawable) {
        if (null == state || null == drawable) {
            throw new RuntimeException("Invalid params");
        }

        mDrawableStateList.put(state, drawable);
        refresh();
    }

    private void onLoadError() {
        onStateChanged(State.ERROR);
    }

    private void onStateChanged(State state) {
        mState = state;
        refresh();
    }

    private void refresh() {
        Drawable drawable = mDrawableStateList.get(mState);
        if (null != drawable) {
            setImageDrawable(drawable);
        }
    }

    public void setImageDrawable(Drawable drawable) {
        if (null != mCallbackRf.get()) {
            mCallbackRf.get().onNotify(drawable);
        }
    }
}
