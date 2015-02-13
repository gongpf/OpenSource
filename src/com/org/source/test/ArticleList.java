package com.org.source.test;

import java.util.List;

import com.org.source.greendao.dao.Article;

public class ArticleList {
    private List<Article> article;
    
    public void setArticle(List<Article> article) {
        this.article = article;
    }
    
    public List<Article> getArticle() {
        return article;
    }
}
