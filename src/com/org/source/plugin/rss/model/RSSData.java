package com.org.source.plugin.rss.model;

import java.util.List;

import com.org.source.activeandroid.Model;
import com.org.source.activeandroid.annotation.Column;
import com.org.source.activeandroid.annotation.Table;
import com.org.source.activeandroid.query.Select;

@Table(name = "rssdata")
public class RSSData extends Model
{
    @Column(name = "url")
    public String url;
    
    @Column(name = "title")
    public String title;
    
    @Column(name = "time")
    public String time;
    
    @Column(name = "imageurl")
    public String imageUrl = "";
    
    public RSSData()
    {
        super();
    }
    
    public RSSData(String url, String title, String time)
    {
        super();
        this.url = url;
        this.title = title;
        this.time = time;
    }
    
    public List<RSSItem> items()
    {
        return getMany(RSSItem.class, "rssdata");
    }
    
    public static RSSData save(String url, String title, String time)
    {
        RSSData data = new Select().from(RSSData.class).where("url = ?", url).executeSingle();
        
        if (null == data)
        {
            data = new RSSData(url, title, time);
        }
        else 
        {
            data.time = time;
            data.title = title;
        }
        
        data.save();
        return data;
    }
}
