package com.eduardocasas.app.service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class ResponseService {
    
    public static ArrayList<Map> getList(ResultSet collection) throws Exception {
        ResultSetMetaData rsmd = collection.getMetaData();
        int columns_number = rsmd.getColumnCount();
        ArrayList<Map> list = new ArrayList<Map>();
        while (collection.next()) {
            Map<String,String> item = new HashMap<String,String>();
            for (int i = 1; i <= columns_number; ++i) {
                item.put(rsmd.getColumnLabel(i), collection.getString(i));
            }            
            list.add(item);
        }
        
        return list;
    }
    
    public static Map<String,String> getItem(ResultSet item) throws Exception {
        ResultSetMetaData rsmd = item.getMetaData();
        int columns_number = rsmd.getColumnCount();
        Map<String,String> data = new HashMap<String,String>();
        item.first();
        for (int i = 1; i <= columns_number; ++i) {
            data.put(rsmd.getColumnLabel(i), item.getString(i));
        }
        
        return data;
    }
    
}
