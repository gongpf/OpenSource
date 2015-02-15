package com.org.source.sm;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.org.source.base.ContextManager;
import com.org.source.window.Window;
import com.org.source.window.AbstractWindowSwiper.OnInterceptMoveEventListener;

public class WebViewWindow extends Window
{
    private WebView mWebView;
    
    public WebViewWindow(){
        super(ContextManager.getContext());
        mWebView = new WebView(ContextManager.getContext());
        setContentView(mWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        setInterceptMoveEventListener(mInterceptMoveEventListener);
    }
    
    private OnInterceptMoveEventListener mInterceptMoveEventListener = new OnInterceptMoveEventListener()
    {
        @Override
        public boolean isViewDraggableHorizontally(View v, int dx, int x, int y)
        {
            return false;
        }

        @Override
        public boolean isViewDraggableVertically(View v, int dy, int x, int y)
        {
            return true;
        }
    };
    
    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }
    
    @Override
    public void onWindowAttached()
    {
    }

    @Override
    public void onWindowDetached()
    {
    }

}
