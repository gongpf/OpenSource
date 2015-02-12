package com.greendao.generator;

import com.greendao.generator.lib.DaoGenerator;
import com.greendao.generator.lib.Entity;
import com.greendao.generator.lib.Property;
import com.greendao.generator.lib.Schema;
import com.greendao.generator.lib.ToMany;
  
/** 
 * Generates entities and DAOs for the example project DaoExample. 
 *  
 * Run it as a Java application (not Android). 
 *  
 * @author Markus 
 */  
public class DaoGeneratorMain {  
  
    public static void main(String[] args) throws Exception {  
          
        Schema schema = new Schema(3, "com.org.source.greendao.dao");  
  
        addNote(schema);  
        addCustomerOrder(schema);  
  
        new DaoGenerator().generateAll(schema, "../src");  
    }  
  
    private static void addNote(Schema schema) {  
        Entity note = schema.addEntity("Note");  
        note.addIdProperty();  
        note.addStringProperty("text").notNull();  
        note.addStringProperty("comment");  
        note.addDateProperty("date");  
    }  
  
    private static void addCustomerOrder(Schema schema) {  
        Entity customer = schema.addEntity("Customer");  
        customer.addIdProperty();  
        customer.addStringProperty("name").notNull();  
  
        Entity order = schema.addEntity("Order");  
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword  
        order.addIdProperty();  

        Property orderDate = order.addDateProperty("date").getProperty();  
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();  

        order.addToOne(customer, customerId);  
  
        ToMany customerToOrders = customer.addToMany(order, customerId);  
        customerToOrders.setName("orders");  
        customerToOrders.orderAsc(orderDate);  
    }  
  
}  
