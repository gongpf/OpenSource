package com.org.source.sm;

import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import android.os.AsyncTask;

import com.google.gson.Gson;

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

        Gson gson = new Gson();
        T result = null;

        try {
            result = gson.fromJson(new InputStreamReader(new URL(params[0]).openStream()), classT);
        } catch (Exception e) {
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
