package com.org.source.rss;


import android.app.Activity;
import android.os.Bundle;

import com.org.source.main.HomeWindow;
import com.org.source.window.WindowManager;


public class RSSActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager mWindowManager = new WindowManager(this);
        mWindowManager.pushWindow(new HomeWindow(this));
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
}