package com.org.source.sm.model;

import java.util.List;

public class ChannelList {
    private List<Channel> channel;

    public void setArticle(List<Channel> channels) {
        this.channel = channels;
    }

    public List<Channel> getChannel() {
        return channel;
    }
    
    public void save() {
        if (null != channel && channel.size() > 0) {
            DaoHelper.getDaoSession().getChannelDao().getDatabase().beginTransaction();
            
            for (Channel channelItem : channel) {
                channelItem.save();
            }
            
            DaoHelper.getDaoSession().getChannelDao().getDatabase().setTransactionSuccessful();
            DaoHelper.getDaoSession().getChannelDao().getDatabase().endTransaction();
        }
    }

}
