package com.org.source.sm;

import java.lang.ref.WeakReference;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.org.source.jackson.databind.DeserializationFeature;
import com.org.source.jackson.databind.ObjectMapper;

public class SMRequestAsynTask<T> extends AsyncTask<String, Void, T>{
    
    private final Class<T> classT;
    private final WeakReference<SMRequestCallBack<T>> mCallbackRf;
    
    public SMRequestAsynTask(Class<T> classT, SMRequestCallBack<T> callBack)
    {
        this.classT = classT;
        mCallbackRf = new WeakReference<SMRequestCallBack<T>>(callBack);
    }
    
    @Override
    protected T doInBackground(String... params)
    {
        if (null == params || params.length < 1) {
            throw new RuntimeException("invalid params");
        }
        
        T result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try
        {
            long start = System.currentTimeMillis(); 
            result = objectMapper.readValue(new URL(params[0]), classT);
            Log.e("json", "time:" + (System.currentTimeMillis() - start));
        }
        catch(Exception e)
        {
            Log.e("json", e.toString());
        }
        
        return result;
    }
    
    @Override
    protected void onPostExecute(T result) {
        if (null != mCallbackRf.get()) {
            mCallbackRf.get().onFinished(result);
        }
    }
    
    public static interface SMRequestCallBack<T> {
        
        public void onFinished(T result);
    }
}
