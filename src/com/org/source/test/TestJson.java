package com.org.source.test;

import java.io.IOException;
import java.net.URL;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestJson {
    
    public void run() {
        String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channel/923258246?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";

        ObjectMapper objectMapper = new ObjectMapper();
        // to prevent exception when encountering unknown property:
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try
        {
            long start = System.currentTimeMillis();
            ChannelJsonResonse acc = objectMapper.readValue(new URL(baseUrl),
                    ChannelJsonResonse.class);
            Log.e("json", "time:" + (System.currentTimeMillis() - start));
            Log.e("json", acc.toString());
        }
        catch(JsonParseException e)
        {
            e.printStackTrace();
            Log.e("json", e.toString());
        }
        catch(JsonMappingException e)
        {
            e.printStackTrace();
            Log.e("json", e.toString());
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Log.e("json", e.toString());
        }
    }
    
//    02-14 12:50:05.215: E/json(11351): org.codehaus.jackson.map.JsonMappingException: 
//        Entity is detached from DAO context (through reference chain: 
//            com.org.source.test.ChannelJsonResonse["data"]->com.org.source.test.ArticleList["article"]->
//            com.org.source.greendao.dao.Article["image"])
//    
//    02-14 12:53:32.213: E/json(11351): org.codehaus.jackson.map.JsonMappingException: Can not construct 
//    instance of java.lang.Long from String value '14396379522958101729': not a valid Long value
//    
//    02-14 12:53:32.213: E/json(11351):  at [Source: http://zzd.sm.cn/appservice/api/v1/channel/923258246?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2; 
//        line: 1, column: 22] (through reference chain:
//            com.org.source.test.ChannelJsonResonse["data"]->
//    com.org.source.test.ArticleList["article"]->
//    com.org.source.greendao.dao.Article["id"])
    
    public void test() {
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TestJson().run();
            }
        }).start();
    }

}
