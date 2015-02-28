package com.org.source.sm.model;

import android.database.sqlite.SQLiteDatabase;

import com.org.source.base.ContextManager;
import com.org.source.sm.model.DaoMaster.DevOpenHelper;

public class DaoHelper {
    
    private static DaoSession sDaoSession;
    private static SQLiteDatabase sDatabase;
    private static DaoMaster sDaoMaster;
    
    static private void init() {
        DevOpenHelper helper = new DaoMaster.DevOpenHelper(ContextManager.getContext(), "sm-db", null);
        sDatabase = helper.getWritableDatabase();
        sDaoMaster = new DaoMaster(sDatabase);
        sDaoSession = sDaoMaster.newSession();
    }
    
    static public DaoSession getDaoSession() {
        if (null == sDaoSession) {
           init(); 
        }
        return sDaoSession;
    }
}
