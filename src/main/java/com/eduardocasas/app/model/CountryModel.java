package com.eduardocasas.app.model;

import com.eduardocasas.app.service.LogService;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class CountryModel extends Database {

    public CountryModel() throws Exception {
        connect();
    }
    
    public ResultSet getCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT id, name FROM country");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
}
