package com.org.source.main;


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.org.source.test.SMJsonResponse;
import com.org.source.test.SMRequestAsynTask;
import com.org.source.test.ChannelJsonResonse;;


public class MainActivity extends Activity {

    private Controller mController;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mController = new Controller(this);
//        mController.start(this);
        
        String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channel/923258246?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";
        new SMRequestAsynTask<ChannelJsonResonse>(ChannelJsonResonse.class).execute(baseUrl);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mController.destory();
    }
}