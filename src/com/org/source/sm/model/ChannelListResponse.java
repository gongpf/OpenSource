package com.org.source.sm.model;

import com.org.source.sm.model.SMJsonResponse.Result;

public class ChannelListResponse {
    
    private ChannelList data;
    private Result result;
    
    public void setData(ChannelList data) {
        this.data = data;
    }
    
    public ChannelList getData() {
        return data;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public Result getResult() {
        return result;
    }
}
