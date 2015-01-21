package com.org.source.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends Activity {

    private Controller mController;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mController = new Controller(this);
        mController.start(this);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mController.destory();
    }
}