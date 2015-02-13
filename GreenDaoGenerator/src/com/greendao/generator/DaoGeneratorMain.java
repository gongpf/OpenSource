package com.greendao.generator;

import com.greendao.generator.lib.DaoGenerator;
import com.greendao.generator.lib.Entity;
import com.greendao.generator.lib.Property;
import com.greendao.generator.lib.Schema;
  
/** 
 * Generates entities and DAOs for the example project DaoExample. 
 *  
 * Run it as a Java application (not Android). 
 *  
 * @author Markus 
 */  
public class DaoGeneratorMain {  
  
    private static Entity channel;
    private static Entity article;
    private static Entity articleImage;
    private static Entity articleThumbnail;

    public static void main(String[] args) throws Exception {  
          
        Schema schema = new Schema(3, "com.org.source.greendao.dao", "com.org.source.greendao");  
  
        addChannel(schema);
        addArticle(schema);
        addArticleImage(schema);
        addArticleThumbnail(schema);

        new DaoGenerator().generateAll(schema, "../src");  

        Schema schema1 = new Schema(3, "com.org.source.test", "com.org.source.greendao");  
        addTestBean(schema1);
        new DaoGenerator().generateAll(schema1, "../src");  
    }  

    private static void addTestBean(Schema schema) {  
        Entity channel = schema.addEntity("Channel");  
        channel.addIdProperty();  
        channel.addStringProperty("address");  
        channel.addStringProperty("name");  
        channel.addShortProperty("cid");  
        channel.addBooleanProperty("open");  
    }
  
    private static void addChannel(Schema schema) {  
        channel = schema.addEntity("Channel");  
        channel.addIdProperty();  
        channel.addStringProperty("name").notNull();  
        channel.addShortProperty("status");
        channel.addBooleanProperty("is_fixed");
        channel.addBooleanProperty("is_subscribed");
        channel.addShortProperty("show_type");
    }  

    private static void addArticle(Schema schema) {  
        article = schema.addEntity("Article");  
        article.addIdProperty().getProperty();
        article.addStringProperty("attribute");  
        article.addShortProperty("comment_cnt");
        article.addStringProperty("content").getProperty();
        article.addShortProperty("content_type");
        article.addStringProperty("grab_time");
        
        Property articleCidProperty = article.addLongProperty("cid").notNull().getProperty();
        channel.addToMany(article, articleCidProperty).setName("articles");

        // add images

        article.addShortProperty("item_type");
        article.addStringProperty("matched_tag");
        article.addShortProperty("oppose_cnt");
        article.addStringProperty("original_url");
        article.addStringProperty("publish_time");
        article.addShortProperty("reco_type");
        article.addLongProperty("recoid");
        article.addStringProperty("site");
        article.addStringProperty("source_name");
        
        // add special

        article.addStringProperty("summary");
        article.addShortProperty("support_cnt");
        article.addStringProperty("tags");
        
        // add thumbanils

        article.addStringProperty("title");
        article.addStringProperty("url");
        article.addBooleanProperty("valid");
    }  

    private static void addArticleImage(Schema schema) {  
        articleImage = schema.addEntity("ArticleImage");  
        articleImage.addIdProperty().autoincrement();  
        articleImage.addStringProperty("title");  
        articleImage.addShortProperty("index");
        articleImage.addStringProperty("description");  
        articleImage.addShortProperty("width");
        articleImage.addShortProperty("height");
        articleImage.addStringProperty("type");  
        articleImage.addStringProperty("url");  
        articleImage.addStringProperty("gallery_id");  
        articleImage.addShortProperty("gallery_type");

        Property articleIdProperty = articleImage.addLongProperty("aid").notNull().getProperty();
        article.addToMany(articleImage, articleIdProperty, "image");
    }  

    private static void addArticleThumbnail(Schema schema) {  
        articleThumbnail = schema.addEntity("ArticleThumbnail");  
        articleThumbnail.addIdProperty().autoincrement();  
        articleThumbnail.addShortProperty("width");
        articleThumbnail.addShortProperty("height");
        articleThumbnail.addStringProperty("url");  

        Property articleIdProperty = articleThumbnail.addLongProperty("aid").notNull().getProperty();
        article.addToMany(articleThumbnail, articleIdProperty, "thumbnails");
    }  
}  
