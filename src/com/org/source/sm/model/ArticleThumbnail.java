package com.org.source.sm.model;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ARTICLE_THUMBNAIL.
 */
public class ArticleThumbnail {

    private Long id;
    private Short width;
    private Short height;
    private String url;
    /** Not-null value. */
    private String aid;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ArticleThumbnail() {
    }

    public ArticleThumbnail(Long id) {
        this.id = id;
    }

    public ArticleThumbnail(Long id, Short width, Short height, String url, String aid) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.url = url;
        this.aid = aid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getWidth() {
        return width;
    }

    public void setWidth(Short width) {
        this.width = width;
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /** Not-null value. */
    public String getAid() {
        return aid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAid(String aid) {
        this.aid = aid;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
