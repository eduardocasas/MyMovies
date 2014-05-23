package com.eduardocasas.app.model;

import com.eduardocasas.app.service.LogService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import java.io.File;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class Database {

    private String DRIVER;
    private String SERVER;
    private String USER;
    private String PASSWORD;
    
    protected Connection Connection;
    protected PreparedStatement PreparedStatement;
    
    public Database() throws Exception {
        setParameters();        
    }
    
    public void connect() throws Exception {
        try {
            Class.forName(DRIVER);
            Connection = DriverManager.getConnection(SERVER, USER, PASSWORD);
        } catch (SQLException e) {
            LogService.insert(e);
        }
    }
    
    private void setParameters() throws Exception {
        Ini ini = new Ini(new File("src/main/resources/parameters.ini"));
        Section section = ini.get("data_base");
        DRIVER = section.get("DRIVER");
        SERVER = section.get("SERVER");
        USER = section.get("USER");
        PASSWORD = section.get("PASSWORD");
    }

}
