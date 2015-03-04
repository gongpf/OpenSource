package com.org.source.sm.model;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.org.source.greendao.AbstractDao;
import com.org.source.greendao.Property;
import com.org.source.greendao.internal.DaoConfig;
import com.org.source.greendao.query.Query;
import com.org.source.greendao.query.QueryBuilder;

import com.org.source.sm.model.ArticleThumbnail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ARTICLE_THUMBNAIL.
*/
public class ArticleThumbnailDao extends AbstractDao<ArticleThumbnail, Long> {

    public static final String TABLENAME = "ARTICLE_THUMBNAIL";

    /**
     * Properties of entity ArticleThumbnail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Width = new Property(1, Short.class, "width", false, "WIDTH");
        public final static Property Height = new Property(2, Short.class, "height", false, "HEIGHT");
        public final static Property Url = new Property(3, String.class, "url", false, "URL");
        public final static Property Aid = new Property(4, String.class, "aid", false, "AID");
    };

    private Query<ArticleThumbnail> article_ThumbnailsQuery;

    public ArticleThumbnailDao(DaoConfig config) {
        super(config);
    }
    
    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ARTICLE_THUMBNAIL' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'WIDTH' INTEGER," + // 1: width
                "'HEIGHT' INTEGER," + // 2: height
                "'URL' TEXT," + // 3: url
                "'AID' TEXT NOT NULL );"); // 4: aid
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ARTICLE_THUMBNAIL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ArticleThumbnail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Short width = entity.getWidth();
        if (width != null) {
            stmt.bindLong(2, width);
        }
 
        Short height = entity.getHeight();
        if (height != null) {
            stmt.bindLong(3, height);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(4, url);
        }
        stmt.bindString(5, entity.getAid());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ArticleThumbnail readEntity(Cursor cursor, int offset) {
        ArticleThumbnail entity = new ArticleThumbnail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1), // width
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2), // height
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // url
            cursor.getString(offset + 4) // aid
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ArticleThumbnail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setWidth(cursor.isNull(offset + 1) ? null : cursor.getShort(offset + 1));
        entity.setHeight(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2));
        entity.setUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAid(cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ArticleThumbnail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ArticleThumbnail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "thumbnails" to-many relationship of Article. */
    public List<ArticleThumbnail> _queryArticle_Thumbnails(String aid) {
        synchronized (this) {
            if (article_ThumbnailsQuery == null) {
                QueryBuilder<ArticleThumbnail> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Aid.eq(null));
                article_ThumbnailsQuery = queryBuilder.build();
            }
        }
        Query<ArticleThumbnail> query = article_ThumbnailsQuery.forCurrentThread();
        query.setParameter(0, aid);
        return query.list();
    }

}
