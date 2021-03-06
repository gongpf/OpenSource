package com.org.source.sm.model;


import java.util.List;
// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table TAG.
 */
public class Tag {

    /** Not-null value. */
    private String tag;

    private List<ArticleToTag> articleToTag;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Tag() {
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    /** Not-null value. */
    public String getTag() {
        return tag;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ArticleToTag> getArticleToTag() {
        if (articleToTag == null) {
            ArticleToTagDao targetDao = DaoHelper.getDaoSession().getArticleToTagDao();
            List<ArticleToTag> articleToTagNew = targetDao._queryTag_ArticleToTag(tag);
            synchronized (this) {
                if(articleToTag == null) {
                    articleToTag = articleToTagNew;
                }
            }
        }
        return articleToTag;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetArticleToTag() {
        articleToTag = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        DaoHelper.getDaoSession().getTagDao().delete(this);  
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        DaoHelper.getDaoSession().getTagDao().update(this);  
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        DaoHelper.getDaoSession().getTagDao().refresh(this);  
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
