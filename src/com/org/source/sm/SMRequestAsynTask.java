package com.org.source.sm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import flexjson.JSONDeserializer;

public class SMRequestAsynTask<T> extends AsyncTask<String, Void, T> {

    private final Class<T> classT;
    private final WeakReference<SMRequestCallBack<T>> mCallbackRf;

    public SMRequestAsynTask(Class<T> classT, SMRequestCallBack<T> callBack) {
        this.classT = classT;
        mCallbackRf = new WeakReference<SMRequestCallBack<T>>(callBack);
    }

    @Override
    protected T doInBackground(String... params) {
        if (null == params || params.length < 1) {
            throw new RuntimeException("invalid params");
        }

//        T result2 = null;
//        Gson gson = new Gson();
//        try {
//            long time = System.currentTimeMillis();
//            result2 = gson.fromJson(new InputStreamReader(new URL(params[0]).openStream()), classT);
//            Log.e("json", "Gson:" + (System.currentTimeMillis() - time));
//        } catch (Exception e) {
//        }

//        T result1 = null;
//        try {
//            long time2 = System.currentTimeMillis();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(params[0]).openStream()));
//            result1 = new JSONDeserializer<T>().deserialize(reader, classT);
//            Log.e("json", "Flexjson:" + (System.currentTimeMillis() - time2));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        return null;
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
