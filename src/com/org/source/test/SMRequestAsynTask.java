package com.org.source.test;

import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SMRequestAsynTask<T> extends AsyncTask<String, Void, T>{
    
    private final Class<T> classT;
    
    public SMRequestAsynTask(Class<T> classT)
    {
        this.classT = classT;
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
}
