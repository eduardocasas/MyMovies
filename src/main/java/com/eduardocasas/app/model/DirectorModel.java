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
public class DirectorModel extends Database {

    public DirectorModel() throws Exception {
        connect();
    }
        
    public int getTotalNum() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT COUNT(*) as total FROM director");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        ResultSet result = PreparedStatement.executeQuery();
        result.first();

        return result.getInt("total");
    }
    
    public ResultSet getCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT director.id, person_id, person.name, surname, birthday, person.picture, country_id, country.picture AS country_picture, country.name AS country_name FROM director LEFT OUTER JOIN person ON director.person_id = person.id LEFT OUTER JOIN country ON person.country_id = country.id");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getCollection(int movie_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT director.id, person_id, person.name, surname, birthday, person.picture, country_id, country.picture AS country_picture, country.name AS country_name FROM director LEFT OUTER JOIN person ON director.person_id = person.id LEFT OUTER JOIN country ON person.country_id = country.id LEFT OUTER JOIN movie_director ON director.id = movie_director.director_id WHERE movie_director.movie_id = ?");
            PreparedStatement.setInt(1, movie_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public void editMovies(String[] directors,  int movie_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM movie_director WHERE movie_id = ?");
        PreparedStatement.setInt(1, movie_id);
        PreparedStatement.executeUpdate();
        addMovies(directors, movie_id);
    }

    public void editMovies(int director_id, String[] movies) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM movie_director WHERE director_id = ?");
        PreparedStatement.setInt(1, director_id);
        PreparedStatement.executeUpdate();
        addMovies(director_id, movies);
    }

    public void addMovies(String[] directors, int movie_id) throws Exception {
        if (directors != null) {
            PreparedStatement = Connection.prepareStatement("INSERT INTO movie_director (movie_id, director_id) VALUES (?, ?)");
            for (String director_id : directors) {
                PreparedStatement.setInt(1, movie_id);
                PreparedStatement.setInt(2, Integer.parseInt(director_id));
                PreparedStatement.addBatch();
            }
            PreparedStatement.executeBatch();
        }
    }
    
    public void addMovies(int director_id, String[] movies) throws Exception {
        if (movies != null) {
            PreparedStatement = Connection.prepareStatement("INSERT INTO movie_director (movie_id, director_id) VALUES (?, ?)");
            for (String movie_id : movies) {
                PreparedStatement.setInt(1, Integer.parseInt(movie_id));
                PreparedStatement.setInt(2, director_id);
                PreparedStatement.addBatch();
            }
            PreparedStatement.executeBatch();
        }
    }
    
    public int insert(int person_id) throws Exception {
        int last_inserted_id = 0;
        PreparedStatement = Connection.prepareStatement("INSERT INTO director (person_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement.setInt(1, person_id);
        PreparedStatement .executeUpdate();
        ResultSet rs = PreparedStatement.getGeneratedKeys();
        if(rs.next()) {
            last_inserted_id = rs.getInt(1);
        }

        return last_inserted_id;
    }
    
    public void delete(int director_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM director WHERE id = ?");
        PreparedStatement.setInt(1, director_id);
        PreparedStatement.executeUpdate();
    }

}
