package com.org.source.daojson;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.os.Debug;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.atom.Person;
import com.google.gson.Gson;
import com.org.source.jackson.databind.DeserializationFeature;
import com.org.source.jackson.databind.ObjectMapper;

import flexjson.JSONDeserializer;

public class DaoJson{
    
    private final ObjectBinder mObjectBinder;
    
    public DaoJson() {
        mObjectBinder = new ObjectBinder();
    }

    public Object fromJson(String json, Type targetType) {
        Object result = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            result = mObjectBinder.bind(jsonObject, targetType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private static String getJson() {
        Person person = new Person();
        List<Phone> list = new ArrayList<DaoJson.Phone>();
        for (int i = 0; i < 1000; i++) {
            Phone phone = new Phone();
            phone.setAge(12);
            phone.setMarried(true);
            phone.setName("weuiuerwiiiiiiiiiiiiiiiiiiiiiiiiito");
            
            list.add(phone);
        }
        person.setPhone(list);
        
        Gson gson = new Gson();
        return gson.toJson(person);
        
    }

    public static void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        try {
            String result = getJson();
            long start = System.currentTimeMillis();
            objectMapper.readValue(result, Person.class);
            Log.e("jackson", "time:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            Log.e("json", e.toString());
        }
                

        {
//        Debug.startMethodTracing("daojson");
            String result = getJson();
            DaoJson daoJson = new DaoJson();
            Long time = System.currentTimeMillis();
            Person person2 = (Person)daoJson.fromJson(result, Person.class);
            Log.e("Daojson", "" + (System.currentTimeMillis() - time));
//        Debug.stopMethodTracing();
        }

        {
            String result = getJson();
            Long time1 = System.currentTimeMillis();
            Gson gson = new Gson();
            Person person1 = gson.fromJson(result, Person.class);
            Log.e("Gson", "" + (System.currentTimeMillis() - time1));
        }
        
        try {
            String result = getJson();
            Long time9 = System.currentTimeMillis();
            JSONObject jsonObject  = new JSONObject(result);
            Person person3 = new Person();
            
            JSONArray jsonArray = jsonObject.getJSONArray("phone");
            int length = jsonArray.length();
            List<Phone> list2 = new ArrayList<DaoJson.Phone>();
            for (int i = 0; i < length; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Phone phone = new Phone();
                phone.setAge(object.getInt("age"));
                phone.setMarried(object.getBoolean("married"));
                phone.setName(object.getString("name"));
                list2.add(phone);
            }
            person3.setPhone(list2);
            Log.e("jsonobject", "" + (System.currentTimeMillis() - time9));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        {
            String result = getJson();
            Long time11 = System.currentTimeMillis();
            JSONDeserializer<Person> jsonDeserializer = new JSONDeserializer<DaoJson.Person>();
            Person person4 = jsonDeserializer.deserialize(result, Person.class);
            Log.e("flexjsonobject", "" + (System.currentTimeMillis() - time11));
        }

    }
    
    public static class Phone{
        private String name;
        private int age;
        private boolean married;
        
        public void setAge(int age) {
            this.age = age;
        }
        
        public void setMarried(boolean married) {
            this.married = married;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Person {
        private List<Phone> phone;

        public void setPhone(List<Phone> phone) {
            this.phone = phone;
        }
    }
}
