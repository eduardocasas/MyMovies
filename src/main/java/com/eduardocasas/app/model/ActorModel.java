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
public class ActorModel extends Database {

    public ActorModel() throws Exception {
        connect();
    }
    
    public int getTotalNum() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT COUNT(*) as total FROM actor");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        ResultSet result = PreparedStatement.executeQuery();
        result.first();

        return result.getInt("total");
    }
    
    public ResultSet getCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT actor.id, person_id, person.name, surname, birthday, picture, country_id, country.name AS country_name  FROM actor LEFT OUTER JOIN person ON actor.person_id = person.id LEFT OUTER JOIN country ON person.country_id = country.id");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getCollection(int movie_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT actor.id, person_id, person.name, surname, birthday, picture, country_id, country.name AS country_name  FROM actor LEFT OUTER JOIN person ON actor.person_id = person.id LEFT OUTER JOIN country ON person.country_id = country.id LEFT OUTER JOIN movie_actor ON actor.id = movie_actor.actor_id WHERE movie_actor.movie_id = ?");
            PreparedStatement.setInt(1, movie_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }

    public void editMovies(String[] actors, int movie_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM movie_actor WHERE movie_id = ?");
        PreparedStatement.setInt(1, movie_id);
        PreparedStatement.executeUpdate();
        addMovies(actors, movie_id);
    }
    
    public void editMovies(int actor_id, String[] movies) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM movie_actor WHERE actor_id = ?");
        PreparedStatement.setInt(1, actor_id);
        PreparedStatement.executeUpdate();
        addMovies(actor_id, movies);
    }

    public void addMovies(String[] actors, int movie_id) throws Exception {
        if (actors != null) {
            PreparedStatement = Connection.prepareStatement("INSERT INTO movie_actor (movie_id, actor_id) VALUES (?, ?)");
            for (String actor_id : actors) {
                PreparedStatement.setInt(1, movie_id);
                PreparedStatement.setInt(2, Integer.parseInt(actor_id));
                PreparedStatement.addBatch();
            }
            PreparedStatement.executeBatch();
        }
    }
    
    public void addMovies(int actor_id, String[] movies) throws Exception {
        if (movies != null) {
            PreparedStatement = Connection.prepareStatement("INSERT INTO movie_actor (movie_id, actor_id) VALUES (?, ?)");
            for (String movie_id : movies) {
                PreparedStatement.setInt(1, Integer.parseInt(movie_id));
                PreparedStatement.setInt(2, actor_id);
                PreparedStatement.addBatch();
            }
            PreparedStatement.executeBatch();
        }
    }
    
    public int insert(int person_id) throws Exception {
        int last_inserted_id = 0;
        PreparedStatement = Connection.prepareStatement("INSERT INTO actor (person_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement.setInt(1, person_id);
        PreparedStatement .executeUpdate();
        ResultSet rs = PreparedStatement.getGeneratedKeys();
        if(rs.next()) {
            last_inserted_id = rs.getInt(1);
        }

        return last_inserted_id;
    }
    
    public void delete(int actor_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM actor WHERE id = ?");
        PreparedStatement.setInt(1, actor_id);
        PreparedStatement.executeUpdate();
    }
        
}
