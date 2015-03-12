package com.org.source.dao.json;

import android.util.Log;

import com.google.gson.Gson;

public class DaoJsonTest {
    
    private static String getJson() {
        Person person = new Person();
        Phone[] list = new Phone[1001];
        for (int i = 0; i < 1000; i++) {
            Phone phone = new Phone();
            phone.setAge(12);
            phone.setMarried(true);
            phone.setName("weuiuerwiiiiiiiiiiiiiiiiiiiiiiiiito");
            
            list[i] = phone;
        }
        person.setPhone(list);
        
        Gson json = new Gson();
        return json.toJson(person);
    }
    
    public static void test() {
        {
            String result = getJson();
            DaoJson daoJson = new DaoJson();
            Long time = System.currentTimeMillis();
            Person person2 = (Person)daoJson.fromJson(result, Person.class);
            Log.e("Daojson", "" + (System.currentTimeMillis() - time));
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
        private Phone[] phone;

        public void setPhone(Phone[] phone) {
            this.phone = phone;
        }
    }

}
