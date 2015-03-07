package com.org.source.sm.model;

import com.org.source.sm.model.SMJsonResponse.Result;

public class ArticleListResponse {
    
    private ArticleList data;
    private Result result;
    
    public void setData(ArticleList data) {
        this.data = data;
    }
    
    public ArticleList getData() {
        return data;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public Result getResult() {
        return result;
    }

}
