package com.org.source.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.org.source.test.TestJson;


public class MainActivity extends Activity {

    private Controller mController;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mController = new Controller(this);
//        mController.start(this);
        
        new TestJson().test();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mController.destory();
    }
}