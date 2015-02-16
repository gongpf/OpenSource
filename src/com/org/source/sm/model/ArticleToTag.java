package com.org.source.sm.model;

import com.org.source.sm.model.DaoSession;
import com.org.source.greendao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ARTICLE_TO_TAG.
 */
public class ArticleToTag {

    private Long id;
    /** Not-null value. */
    private String aid;
    /** Not-null value. */
    private String tid;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ArticleToTagDao myDao;

    private Tag tag;
    private String tag__resolvedKey;

    private Article article;
    private String article__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ArticleToTag() {
    }

    public ArticleToTag(Long id) {
        this.id = id;
    }

    public ArticleToTag(Long id, String aid, String tid) {
        this.id = id;
        this.aid = aid;
        this.tid = tid;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArticleToTagDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getAid() {
        return aid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAid(String aid) {
        this.aid = aid;
    }

    /** Not-null value. */
    public String getTid() {
        return tid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTid(String tid) {
        this.tid = tid;
    }

    /** To-one relationship, resolved on first access. */
    public Tag getTag() {
        String __key = this.tid;
        if (tag__resolvedKey == null || tag__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            Tag tagNew = targetDao.load(__key);
            synchronized (this) {
                tag = tagNew;
            	tag__resolvedKey = __key;
            }
        }
        return tag;
    }

    public void setTag(Tag tag) {
        if (tag == null) {
            throw new DaoException("To-one property 'tid' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.tag = tag;
            tid = tag.getTag();
            tag__resolvedKey = tid;
        }
    }

    /** To-one relationship, resolved on first access. */
    public Article getArticle() {
        String __key = this.aid;
        if (article__resolvedKey == null || article__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ArticleDao targetDao = daoSession.getArticleDao();
            Article articleNew = targetDao.load(__key);
            synchronized (this) {
                article = articleNew;
            	article__resolvedKey = __key;
            }
        }
        return article;
    }

    public void setArticle(Article article) {
        if (article == null) {
            throw new DaoException("To-one property 'aid' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.article = article;
            aid = article.getId();
            article__resolvedKey = aid;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}