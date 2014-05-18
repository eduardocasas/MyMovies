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
public class PersonModel extends Database {
    
    public PersonModel() throws Exception {
        connect();
    }
    
    public ResultSet getCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT person.id, person.name, person.surname, person.birthday, country.name AS country_name FROM person LEFT OUTER JOIN country ON person.country_id = country.id");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getCollectionByCountry(int country_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT person.id, person.name, surname, birthday, person.picture, country_id, country.name AS country_name FROM person LEFT OUTER JOIN country ON person.country_id = country.id WHERE person.country_id = ?");
            PreparedStatement.setInt(1, country_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getLastItemsCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT person.id, person.name, person.surname, person.birthday, person.picture, country.picture AS country_picture, country_id, country.name AS country_name FROM person LEFT OUTER JOIN country ON person.country_id = country.id ORDER BY person.id DESC LIMIT 0, 20");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getCollection(ResultSet collection) throws Exception {
        String excluded_user_collection = ",0";
        while (collection.next()) {
            excluded_user_collection += ","+collection.getString("person_id");
        }
        collection.beforeFirst();
        excluded_user_collection = excluded_user_collection.substring(1);
        try {
            PreparedStatement = Connection.prepareStatement("SELECT p.id, p.name, p.surname, p.birthday, p.picture, c.name AS country_name FROM person AS p LEFT OUTER JOIN country AS c ON(p.country_id= c.id) WHERE p.id IN("+excluded_user_collection+")=0");
        } catch (SQLException e) {
            LogService.insert(e);
        }

        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getItem(int person_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT person.id, person.name, person.surname, person.birthday, person.country_id, person.picture, country.name AS country_name, director.id AS director_id, actor.id AS actor_id FROM person LEFT OUTER JOIN country ON person.country_id = country.id LEFT OUTER JOIN director ON person.id = director.person_id LEFT OUTER JOIN actor ON person.id = actor.person_id WHERE person.id = ?");
            PreparedStatement.setInt(1, person_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public void edit(int id, String name, String surname, int birthday, int country_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE person set name = ?, surname = ?, birthday = ?, country_id = ? WHERE id = ?");
        PreparedStatement.setString(1, name);
        PreparedStatement.setString(2, surname);
        PreparedStatement.setInt(3, birthday);
        PreparedStatement.setInt(4, country_id);
        PreparedStatement.setInt(5, id);
        PreparedStatement.executeUpdate();
    }
    
    public int insert(String name, String surname, int birthday, int country_id) throws Exception {
        int last_inserted_id = 0;
        PreparedStatement = Connection.prepareStatement("INSERT INTO person (name, surname, birthday, country_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement.setString(1, name);
        PreparedStatement.setString(2, surname);
        PreparedStatement.setInt(3, birthday);
        PreparedStatement.setInt(4, country_id);
        PreparedStatement .executeUpdate();
        ResultSet rs = PreparedStatement.getGeneratedKeys();
        if(rs.next()) {
            last_inserted_id = rs.getInt(1);
        }

        return last_inserted_id;
    }
    
    public void delete(int person_id) throws Exception {
        ResultSet person = getItem(person_id);
        person.first();
        PreparedStatement = Connection.prepareStatement("DELETE FROM person WHERE id = ?");
        PreparedStatement.setInt(1, person_id);
        PreparedStatement.executeUpdate();
        ActorModel Actor = new ActorModel();
        DirectorModel Director = new DirectorModel();
        Actor.delete(person.getInt("actor_id"));
        Director.delete(person.getInt("director_id"));
    }
    
    public void setPictureActive(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE person set picture = 1 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }
    
    public void setPictureNull(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE person set picture = 0 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }

}
