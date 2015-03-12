package com.org.source.dao.json;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.org.source.dao.base.MapToObjectBinder;

public class DaoJson extends MapToObjectBinder{
    
    public Object fromJson(String json, Type targetType) {

        if (TextUtils.isEmpty(json)) {
            return null;
        }

        Object result = null;
        try {
            JsonMapData jsonMapData = new JsonMapData(json);
            result = bind(jsonMapData, targetType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static class JsonArrayData implements IArrayData {
        private final JSONArray mJsonArray;

        public JsonArrayData(JSONArray jsonArray) {
            if (null == jsonArray) {
                throw new RuntimeException("the jsonArray is null");
            }
            mJsonArray = jsonArray;
        }

        @Override
        public int getLength() {
            return mJsonArray.length();
        }

        @Override
        public Object get(int position) {
            Object result = null;
            try {
                result = mJsonArray.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (null != result && result instanceof JSONObject) {
                return new JsonMapData((JSONObject)result);
            }

            if (null != result && result instanceof JSONArray) {
                return new JsonArrayData((JSONArray)result);
            }
            return result;
        }
    }
    
    public static class JsonMapData implements IMapData {
        private final JSONObject mJsonObject;

        public JsonMapData(JSONObject jsonObject) {
            if (null == jsonObject) {
                throw new RuntimeException("the jsonObject is null");
            }
            mJsonObject = jsonObject;
        }
        
       public JsonMapData(String json) throws JSONException {
           mJsonObject = new JSONObject(json);
       }

        @Override
        public Object getValue(String key) {
            Object result = null;
            try {
                result = mJsonObject.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
            if (null != result && result instanceof JSONObject) {
                return new JsonMapData((JSONObject)result);
            }

            if (null != result && result instanceof JSONArray) {
                return new JsonArrayData((JSONArray)result);
            }
            return result;
        }

        @Override
        public boolean has(String key) {
            return mJsonObject.has(key);
        }
    }
}
