package com.org.source.plugin.rss;

import android.text.TextUtils;

import com.org.source.activeandroid.Model;
import com.org.source.activeandroid.annotation.Column;
import com.org.source.activeandroid.annotation.Table;

@Table(name = "rssitem")
public class RSSItem extends Model
{
    @Column(name = "originurl")
    public String originUrl;
    
    @Column(name = "description")
    public String description;
    
    @Column(name = "time")
    public String updateTime;
    
    @Column(name = "imageurl")
    public String imageUrl;
    
    public RSSItem (String origin, String des, String time, String url)
    {
        super();
        
        if (TextUtils.isEmpty(origin))
        {
            throw new RuntimeException("The origin url is invalid");
        }
        
        originUrl = origin;
        description = des;
        updateTime = time;
        imageUrl = url;
    }
    
    public static void save(String origin, String des, String time, String url)
    {
        new RSSItem(origin, des, time, url).save();
    }
}
