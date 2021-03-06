package com.org.source.sm.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.org.source.greendao.AbstractDao;
import com.org.source.greendao.Property;
import com.org.source.greendao.internal.DaoConfig;

import com.org.source.sm.model.Tag;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TAG.
*/
public class TagDao extends AbstractDao<Tag, String> {

    public static final String TABLENAME = "TAG";

    /**
     * Properties of entity Tag.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Tag = new Property(0, String.class, "tag", true, "TAG");
    };


    public TagDao(DaoConfig config) {
        super(config);
    }
    
    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TAG' (" + //
                "'TAG' TEXT PRIMARY KEY NOT NULL );"); // 0: tag
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TAG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Tag entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getTag());
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Tag readEntity(Cursor cursor, int offset) {
        Tag entity = new Tag( //
            cursor.getString(offset + 0) // tag
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Tag entity, int offset) {
        entity.setTag(cursor.getString(offset + 0));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Tag entity, long rowId) {
        return entity.getTag();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Tag entity) {
        if(entity != null) {
            return entity.getTag();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
