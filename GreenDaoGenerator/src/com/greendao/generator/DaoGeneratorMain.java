package com.greendao.generator;

import com.greendao.generator.lib.DaoGenerator;
import com.greendao.generator.lib.Entity;
import com.greendao.generator.lib.Property;
import com.greendao.generator.lib.Schema;
import com.greendao.generator.lib.ToOne;
  
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
    private static Entity tags;
    private static Entity articleToTag;

    public static void main(String[] args) throws Exception {  
          
        Schema schema = new Schema(3, "com.org.source.sm.model", "com.org.source.greendao");  
        schema.enableKeepSectionsByDefault();
        addChannel(schema);
        new DaoGenerator().generateAll(schema, "../src");  
    }  

    private static void addChannel(Schema schema) {  
        //1.1 channel
        channel = schema.addEntity("Channel");  
        channel.addIdProperty();  
        channel.addStringProperty("name").notNull();  
        channel.addShortProperty("status");
        channel.addBooleanProperty("is_fixed");
        channel.addBooleanProperty("is_subscribed");
        channel.addShortProperty("show_type");
        
        //1.2.0 tag
        tags = schema.addEntity("Tag");
        tags.addStringProperty("tag").notNull().primaryKey().getProperty();  
        
        //1.2.1 article-tag
        articleToTag = schema.addEntity("ArticleToTag");
        articleToTag.addIdProperty();
        Property propertyAidInarticleToTag = articleToTag.addStringProperty("aid").notNull().getProperty();
        Property propertyTagIdInarticleToTag = articleToTag.addStringProperty("tid").notNull().getProperty();
        
        //1.2.2 article
        article = schema.addEntity("Article");  
        Property articleIdProperty = article.addStringProperty("id").notNull().primaryKey().getProperty();
        article.addStringProperty("attribute");  
        article.addShortProperty("comment_cnt");
        article.addStringProperty("content").getProperty();
        article.addShortProperty("content_type");
        article.addStringProperty("grab_time");
        
        Property articleCidProperty = article.addLongProperty("cid").notNull().getProperty();
        channel.addToMany(article, articleCidProperty).setName("articles");

        //
        //article.addStringProperty("matched_tag");
        //
        
        article.addShortProperty("item_type");
        article.addShortProperty("oppose_cnt");
        article.addStringProperty("original_url");
        article.addStringProperty("publish_time");
        article.addShortProperty("reco_type");
        article.addStringProperty("recoid");
        article.addStringProperty("site");
        article.addStringProperty("source_name");
        
        // add special
        //article.addStringProperty("special_content");
        //

        article.addStringProperty("summary");
        article.addShortProperty("support_cnt");
        
        article.addStringProperty("matched_tag");
        
        // add images
        // add thumbanils

        article.addStringProperty("title");
        article.addStringProperty("url");
        article.addBooleanProperty("valid");


        articleToTag.addToOne(tags, propertyTagIdInarticleToTag);
        articleToTag.addToOne(article, propertyAidInarticleToTag);
        article.addToMany(articleToTag, propertyAidInarticleToTag).setName("articleToTag");
        tags.addToMany(articleToTag, propertyTagIdInarticleToTag).setName("articleToTag");
        
        //1.3 articleImage
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

        Property articleIdPropertyInImage = articleImage.addStringProperty("aid").notNull().getProperty();
        article.addToMany(articleIdProperty, articleImage, articleIdPropertyInImage).setName("image");
        
        //1.4 articleeThumbnaill
        articleThumbnail = schema.addEntity("ArticleThumbnail");  
        articleThumbnail.addIdProperty().autoincrement();  
        articleThumbnail.addShortProperty("width");
        articleThumbnail.addShortProperty("height");
        articleThumbnail.addStringProperty("url");  

        Property articleIdPropertyInThumbnail = articleThumbnail.addStringProperty("aid").notNull().getProperty();
        article.addToMany(articleIdProperty, articleThumbnail, articleIdPropertyInThumbnail).setName("thumbnails");
        
        //1.5 banner
    }  
}  
