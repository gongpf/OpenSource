package com.org.source.plugin.rss.model;

import com.org.source.activeandroid.Model;
import com.org.source.activeandroid.annotation.Column;
import com.org.source.activeandroid.annotation.Table;
import com.org.source.activeandroid.query.Select;

@Table(name = "rssitem")
public class RSSItem extends Model
{
    @Column(name = "rssdata")
    public RSSData rssData;
    
    @Column(name = "description")
    public String description;
    
    @Column(name = "time")
    public String updateTime;
    
    @Column(name = "imageurl")
    public String imageUrl;
    
    @Column(name = "text")
    public String text;
    
    public RSSItem()
    {
        super();
    }
    
    public RSSItem (RSSData rssData, String des, String time, String url, String text)
    {
        super();
        
        this.rssData = rssData;
        this.description = des;
        this.updateTime = time;
        this.imageUrl = url;
        this.text = text;
    }
    
    public static RSSItem save(RSSData rssData, String des, String time, String url, String text)
    {
        RSSItem data = new Select().from(RSSItem.class).where("description = ?", des).executeSingle();
        
        if (null == data)
        {
            data = new RSSItem(rssData, des, time, url, text);
        }
        else 
        {
            data.updateTime = time;
            data.imageUrl = url;
        }
        
        data.save();
        return data;
    }
}
