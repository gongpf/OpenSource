package com.org.source.plugin.rss;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndContent;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;

public class HtmlUtil 
{
    public static List<String> getImageUrlList(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return null;
        }

        RssImageGetter getter = new RssImageGetter();
        Html.fromHtml(text, getter, null);
        return getter.getImageUrlList();
    }

    public static String getUrl(SyndEntry entry)
    {
        String textString = getContent(entry);
        
        List<String> list = getImageUrlList(textString);
        
        if (list == null || list.size() <= 0)
        {
            return null;
        }
        
        return list.get(0);
    }

    
    public static String getContent(SyndEntry entry)
    {
        if (null == entry)
        {
            return null;
        }
        
        SyndContent description = entry.getDescription();
        String result = ""; 

        List contents = entry.getContents();

        if (null != contents && contents.size() > 0)
        {
            for (Object item : contents)
            {
                SyndContent content = (SyndContent) item;
                result += content.getValue();
            }
        }

        if (TextUtils.isEmpty(result))
        {
            result = null != description ? description.getValue() : "";
        }
        
        return result;
    }
        
    public static class RssImageGetter implements ImageGetter
    {
        public List<String> mList = new ArrayList<String>();

        @Override
        public Drawable getDrawable(String source)
        {
            if (TextUtils.isEmpty(source))
            {
                return null;
            }
            
            mList.add(source);

            return null;
        }

        public List<String> getImageUrlList()
        {
            return mList;
        }
    }
}
