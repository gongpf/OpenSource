package com.org.source.test;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

public class TestJson {
    
    public void run() {
     String baseUrl = "http://zzd.sm.cn/appservice/api/v1/channel/923258246?client_os=android&client_version=1.8.0.1&bid=800&m_ch=006&city=020&sn=409863a83890f78ede8da3c44f20d27a&ftime=1423794052009&recoid=16155304276489967791&count=2&method=new&content_cnt=2";  
        
      HttpGet getMethod = new HttpGet(baseUrl);  
                    
      HttpClient httpClient = new DefaultHttpClient();  
        
      try {  
          HttpResponse response = httpClient.execute(getMethod); //发起GET请求  
        
          Log.e("JSON", "resCode = " + response.getStatusLine().getStatusCode()); //获取响应码  
//          Log.e("JSON", "result = " + EntityUtils.toString(response.getEntity(), "utf-8"));//获取服务器响应内容  
          String json = EntityUtils.toString(response.getEntity(), "utf-8");
          Log.e("JSON", "result = " + json);//获取服务器响应内容  
          ObjectMapper objectMapper = new ObjectMapper();
            try {
                long start = System.currentTimeMillis();
                ChannelJsonResonse acc = objectMapper.readValue(json,
                        ChannelJsonResonse.class); 
                Log.e("json", "time:" + (System.currentTimeMillis() - start));
                Log.e("json", acc.toString());
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
      } catch (ClientProtocolException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
      }  
        
    }
    
    public void test() {
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                new TestJson().run();
            }
        }).start();
        
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = "{\"address\":\"address\",\"name\":\"haha\",\"cid\":\"1\",\"open\":true,\"list\":[{\"address\":\"address\",\"name\":\"haha\"},{\"address\":\"address\",\"name\":\"haha\"},{\"address\":\"address\",\"name\":\"haha\"}]}";
//        try {
//            long start = System.currentTimeMillis();
//            Channel acc = objectMapper.readValue(json, Channel.class);
////            ChannelJsonResonse acc = objectMapper.readValue(json, ChannelJsonResonse.class);
//
//            Log.e("json", "time:" + (System.currentTimeMillis() - start));
//
//            Log.e("json", acc.toString());
//        } catch (JsonParseException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
