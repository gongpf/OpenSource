package com.org.source.sm.model;

import java.util.List;

public class ArticleList {
    private List<Article> article;
    
    public void setArticle(List<Article> article) {
        this.article = article;
    }
    
    public List<Article> getArticle() {
        return article;
    }
    
    public void save() {
        if (null != article && article.size() > 0) {
            DaoHelper.getDaoSession().getArticleDao().getDatabase().beginTransaction();
            for (Article articleItem : article) {
                articleItem.save();
            }
            DaoHelper.getDaoSession().getArticleDao().getDatabase().setTransactionSuccessful();
            DaoHelper.getDaoSession().getArticleDao().getDatabase().endTransaction();
        }
    }
}
