package com.eduardocasas.app.model;

import com.eduardocasas.app.service.LogService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class Database {

    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE = "mymovies";
    static final String SERVER = "jdbc:mysql://localhost/"+DATABASE;
    static final String USER = "root";
    static final String PASSWORD = "";
    
    protected Connection Connection;
    protected PreparedStatement PreparedStatement;
    
    public void connect() throws Exception {
        try {
            Class.forName(DRIVER);
            Connection = DriverManager.getConnection(SERVER, USER, PASSWORD);
        } catch (SQLException e) {
            LogService.insert(e);
        }
    }

}
