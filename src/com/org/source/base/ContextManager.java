package com.org.source.base;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class ContextManager
{
    private static Context sContext;
    private static Context sAppContext;

    /**
     * initialize Context Manager before any usage
     * 
     * @param context
     *            this context is share the same life cycle with activity.
     */
    public static void initialize(Context context)
    {
        sContext = context;
        sAppContext = context.getApplicationContext();
    }

    public static void destroy()
    {
        sContext = null;
        // keep the AppContext after destory for app scope invoke.
    }

    // app scope

    public static Context getAppContext()
    {
        return sAppContext;
    }

    public static Resources getResources()
    {
        return sAppContext.getResources();
    }

    public static AssetManager getAssetManager()
    {
        return sAppContext.getAssets();
    }

    public static ContentResolver getContentResolver()
    {
        return sAppContext.getContentResolver();
    }

    public static Object getSystemService(String name)
    {
        if (!TextUtils.isEmpty(name))
        {
            return sAppContext.getSystemService(name);
        }
        return null;
    }

    public static SharedPreferences getSharedPreferences(String name, int mode)
    {
        return sAppContext.getSharedPreferences(name, mode);
    }

    public static PackageManager getPackageManager()
    {
        return sAppContext.getPackageManager();
    }

    public static String getPackageName()
    {
        return sAppContext.getPackageName();
    }

    public static ApplicationInfo getAppInfo()
    {
        return sAppContext.getApplicationInfo();
    }
    
    public static File getCacheDir()
    {
        return sAppContext.getCacheDir();
    }
    
    public static DisplayMetrics getDisplayMetrics()
    {
        return getResources().getDisplayMetrics();
    }

    public static Configuration getConfig()
    {
        return getResources().getConfiguration();
    }

    // activity/service scope

    public static Context getContext()
    {
        return sContext;
    }

    public static Activity getActivity()
    {
        if (sContext instanceof Activity)
        {
            return (Activity) sContext;
        }
        
        return null;
    }

    public static Window getWindow()
    {
        return getActivity().getWindow();
    }

    public static WindowManager getWindowManager()
    {
        return getActivity().getWindowManager();
    }
}