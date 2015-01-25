package com.org.source.plugin.rss;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;
import com.org.source.activeandroid.ActiveAndroid;
import com.org.source.plugin.rss.model.RSSData;
import com.org.source.plugin.rss.model.RSSItem;

public class RssAsynTask extends AsyncTask<Void, Void, Boolean>
{
    private final String mOriginUrl;
    private final WeakReference<RssCallback> mCallbackRf;
    
    public RssAsynTask(String originUrl, RssCallback callback)
    {
        mOriginUrl = originUrl;
        mCallbackRf = new WeakReference<RssCallback>(callback);
    }
    
    @Override
    protected Boolean doInBackground(Void... params)
    {
        if (TextUtils.isEmpty(mOriginUrl))
        {
            throw new RuntimeException("Invalid origin url");
        }

        URLConnection urlConn = null;
        SyndFeed syndFeed = null;
        try
        {
            urlConn = new URL(mOriginUrl).openConnection();
            urlConn.setConnectTimeout(5000);
            SyndFeedInput input = new SyndFeedInput();
            syndFeed = input.build(new XmlReader(urlConn));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        return saveToDB(syndFeed);
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        if (null != mCallbackRf.get())
        {
            mCallbackRf.get().onFinished(result);
        }
    }
    
    private boolean saveToDB(SyndFeed result)
    {
        if (null == result)
        {
            return false;
        }
        
        Date date = result.getPublishedDate();
        RSSData data = RSSData.save(mOriginUrl, result.getTitle(), 
                null != date ? date.toLocaleString() : null);
    
        ActiveAndroid.beginTransaction();
        @SuppressWarnings("unchecked")
        List<SyndEntry> entryList = result.getEntries();
        for (SyndEntry entry : entryList)
        {
            RSSItem.save(data, entry.getTitle(), entry.getPublishedDate().toLocaleString(),
                    HtmlUtil.getUrl(entry), HtmlUtil.getContent(entry));
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
        return true;
    }
}
