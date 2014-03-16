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
public class MovieModel extends Database {
    
    public MovieModel() throws Exception {
        connect();
    }

    public int getTotalNum() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT COUNT(*) as total FROM movie");
        } catch (SQLException e) {
            LogService.insert(e);
        }
        ResultSet result = PreparedStatement.executeQuery();
        result.first();

        return result.getInt("total");
    }
    
    public ResultSet getDirectorCollection(int director_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT movie.id, movie.title, movie.year, movie.score, movie.picture, country.name AS country_name FROM movie LEFT OUTER JOIN country ON movie.country_id = country.id LEFT OUTER JOIN movie_director ON movie.id = movie_director.movie_id WHERE director_id = ?");
            PreparedStatement.setInt(1, director_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }

        return PreparedStatement.executeQuery();    
    }
    
    public ResultSet getActorCollection(int actor_id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT movie.id, movie.title, movie.year, movie.score, movie.picture, country.name AS country_name FROM movie LEFT OUTER JOIN country ON movie.country_id = country.id LEFT OUTER JOIN movie_actor ON movie.id = movie_actor.movie_id WHERE actor_id = ?");
            PreparedStatement.setInt(1, actor_id);
        } catch (SQLException e) {
            LogService.insert(e);
        }

        return PreparedStatement.executeQuery();    
    }
    
    public ResultSet getCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT movie.id, movie.title, movie.year, movie.score, movie.picture, country.name AS country_name FROM movie LEFT OUTER JOIN country ON movie.country_id = country.id");
        } catch (SQLException e) {
            LogService.insert(e);
        }

        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getLastItemsCollection() throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT movie.id, movie.title, movie.year, movie.score, movie.picture, country.name AS country_name FROM movie LEFT OUTER JOIN country ON movie.country_id = country.id ORDER BY movie.id DESC LIMIT 0, 5");
        } catch (SQLException e) {
            LogService.insert(e);
        }

        return PreparedStatement.executeQuery();
    }
    
    public ResultSet getItem(int id) throws Exception {
        try {
            PreparedStatement = Connection.prepareStatement("SELECT movie.id, movie.title, movie.year, movie.score, movie.picture, country.name AS country_name FROM movie LEFT OUTER JOIN country ON movie.country_id = country.id WHERE movie.id = ?");
            PreparedStatement.setInt(1, id);
        } catch (SQLException e) {
            LogService.insert(e);
        }
        
        return PreparedStatement.executeQuery();
    }
    
    public void addDirectors(int movie_id, String[] directors) throws Exception {
        PreparedStatement = Connection.prepareStatement("INSERT INTO movie_director (movie_id, director_id) VALUES (?, ?)");
        for (String director_id : directors) {
            PreparedStatement.setInt(1, movie_id);
            PreparedStatement.setInt(2, Integer.parseInt(director_id));
            PreparedStatement.addBatch();
        }
        PreparedStatement.executeBatch();
    }
    
    public void addActors(int movie_id, String[] actors) throws Exception {
        PreparedStatement = Connection.prepareStatement("INSERT INTO movie_actor (movie_id, actor_id) VALUES (?, ?)");
        for (String actor_id : actors) {
            PreparedStatement.setInt(1, movie_id);
            PreparedStatement.setInt(2, Integer.parseInt(actor_id));
            PreparedStatement.addBatch();
        }
        PreparedStatement.executeBatch();
    }

    public void edit(int id, String title, int year, Float score, int country_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE movie set title = ?, year = ?, score = ?, country_id = ? WHERE id = ?");
        PreparedStatement.setString(1, title);
        PreparedStatement.setInt(2, year);
        PreparedStatement.setFloat(3, score);
        PreparedStatement.setInt(4, country_id);
        PreparedStatement.setInt(5, id);
        PreparedStatement.executeUpdate();
    }
    
    public int insert(String title, int year, Float score, int country_id) throws Exception {
        int last_inserted_id = 0;
        PreparedStatement = Connection.prepareStatement("INSERT INTO movie (title, year, score, country_id) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement.setString(1, title);
        PreparedStatement.setInt(2, year);
        PreparedStatement.setFloat(3, score);
        PreparedStatement.setInt(4, country_id);
        PreparedStatement .executeUpdate();
        ResultSet rs = PreparedStatement.getGeneratedKeys();
        if(rs.next()) {
            last_inserted_id = rs.getInt(1);
        }

        return last_inserted_id;
    }

    public void delete(int movie_id) throws Exception {
        PreparedStatement = Connection.prepareStatement("DELETE FROM movie WHERE id = ?");
        PreparedStatement.setInt(1, movie_id);
        PreparedStatement.executeUpdate();
    }

    public void setPictureActive(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE movie set picture = 1 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }
    
    public void setPictureNull(int id) throws Exception {
        PreparedStatement = Connection.prepareStatement("UPDATE movie set picture = 0 WHERE id = ?");
        PreparedStatement.setInt(1, id);
        PreparedStatement.executeUpdate();
    }

}
