package com.org.source.base;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;

public class AssetHelper
{
    public static InputStream openFile(String path)
    {
        AssetManager assetMgr = ContextManager.getAssetManager();

        try
        {
            return assetMgr.open(path);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static XmlResourceParser openXml(String path) throws IOException
    {
        AssetManager assetMgr = ContextManager.getAssetManager();

        return assetMgr.openXmlResourceParser(path);
    }

    public static boolean fileExists(String path)
    {
        AssetManager assetMgr = ContextManager.getAssetManager();

        try
        {
            /**
             * AssetManager.openFd() may throw FileNotFoundException when file
             * is actually existed but its file descriptor is invalid. So we use
             * AssetManager.open() instead.
             */
            assetMgr.open(path).close();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
