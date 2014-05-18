package com.eduardocasas.app.model;

import com.eduardocasas.app.service.LogService;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            PreparedStatement = Connection.prepareStatement("SELECT id, name, picture FROM country");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getItem(int id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT id, name, picture FROM country WHERE id = ?");
            PreparedStatement.setInt(1, id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public void edit(int id, String name) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE country set name = ? WHERE id = ?");
        PreparedStatement.setString(1, name);
        PreparedStatement.setInt(2, id);
        PreparedStatement.executeUpdate();
    }
    
    public int insert(String name) throws Exception {
        int last_inserted_id = 0;
        PreparedStatement = Connection.prepareStatement("INSERT INTO country (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement.setString(1, name);
        PreparedStatement .executeUpdate();
        ResultSet rs = PreparedStatement.getGeneratedKeys();
        if(rs.next()) {
            last_inserted_id = rs.getInt(1);
        }

        return last_inserted_id;
    }
    
    public void delete(int country_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM country WHERE id = ?");
        PreparedStatement.setInt(1, country_id);
        PreparedStatement.executeUpdate();
    }

    public void setPictureActive(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE country set picture = 1 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }
    
    public void setPictureNull(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE country set picture = 0 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }

}
