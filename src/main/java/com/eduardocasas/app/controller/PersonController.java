package com.eduardocasas.app.controller;

import com.eduardocasas.app.model.PersonModel;
import com.eduardocasas.app.model.MovieModel;
import com.eduardocasas.app.model.ActorModel;
import com.eduardocasas.app.model.DirectorModel;
import com.eduardocasas.app.model.CountryModel;
import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.sql.ResultSet;
import java.io.PrintStream;
import java.io.FileOutputStream;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
@Controller
public class PersonController {

    private static final String PICTURES_PATH = "src/main/webapp/pictures/person/";
            
    private static final int IMG_WIDTH_BIG     = 250;
    private static final int IMG_HEIGHT_BIG    = 250;
    private static final int IMG_WIDTH_MEDIUM  = 180;
    private static final int IMG_HEIGHT_MEDIUM = 180;
    private static final int IMG_WIDTH_SMALL   = 100;
    private static final int IMG_HEIGHT_SMALL  = 100;
    private static final int IMG_WIDTH_TINY    = 60;
    private static final int IMG_HEIGHT_TINY   = 60;
    
            
    @RequestMapping("/person/{id}")
    public String enter(@PathVariable int id, Model model) throws Exception {
        MovieModel Movie = new MovieModel();
        ResultSet person = new PersonModel().getItem(id);
        model.addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()))
             .addAttribute("person", ResponseService.getItem(person))
             .addAttribute("movies", ResponseService.getList(Movie.getCollection()));
        person.first();
        model.addAttribute("directorMovies", ResponseService.getList(Movie.getDirectorCollection(person.getInt("director_id"))))
             .addAttribute("actorMovies", ResponseService.getList(Movie.getActorCollection(person.getInt("actor_id"))));

        return "person/info";
    }
    
    @RequestMapping("/person/{id}/edit")
    public @ResponseBody void edit(
        @PathVariable int id,
        @RequestParam("name") String name,
        @RequestParam("surname") String surname,
        @RequestParam("year") String year,
        @RequestParam("country") String country,
        @RequestParam("actor") int actor_role,
        @RequestParam("director") int director_role,
        @RequestParam(value="actor_movie[]", required=false) String[] actor_movie,
        @RequestParam(value="director_movie[]", required=false) String[] director_movie
    ) throws Exception {
        PersonModel Person = new PersonModel();
        ActorModel Actor = new ActorModel();
        DirectorModel Director = new DirectorModel();
        ResultSet person = Person.getItem(id);
        person.first();
        Person.edit(id, name, surname, Integer.parseInt(year), Integer.parseInt(country));
        if (actor_role == 1) {
            int actor_id = person.getInt("actor_id");
            if (person.wasNull()) {
                actor_id = Actor.insert(id);
            }
            Actor.editMovies(actor_id, actor_movie);
        } else {
            Actor.delete(person.getInt("actor_id"));
        }
        if (director_role == 1) {
            int director_id = person.getInt("director_id");
            if (person.wasNull()) {
                director_id = Director.insert(id);
            }
            Director.editMovies(director_id, director_movie);
        } else {
            Director.delete(person.getInt("director_id"));
        }
    }

    @RequestMapping("/person/{id}/remove")
    public @ResponseBody void remove(@PathVariable int id) throws Exception {
        new PersonModel().delete(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    @RequestMapping(value = "/person/{id}/upload-picture",  headers = "content-type=multipart/*")
    public @ResponseBody void uploadPicture(
            @PathVariable int id,
            @RequestParam("upload_picture") MultipartFile file
    ) throws Exception {
        String path = PICTURES_PATH+Integer.toString(id);
        String file_name = file.getOriginalFilename();
        String file_path = path+"/"+file_name;
        if (!file.isEmpty()) {
            FileService.uploadFile(file, path, file_name);
            FileService.convertImageToJpg(file_path);
            createMultipleImages(file_path, path);
            FileService.removeFile(file_path);
            new PersonModel().setPictureActive(id);
        }
    }
    
    @RequestMapping("/person/{id}/remove-picture")
    public @ResponseBody void removePicture(@PathVariable int id) throws Exception {
        new PersonModel().setPictureNull(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    private static void createMultipleImages(String file_path, String path) {
        FileService.resizeImage(IMG_WIDTH_BIG, IMG_HEIGHT_BIG, file_path, path+"/big.jpg");
        FileService.resizeImage(IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, file_path, path+"/medium.jpg");
        FileService.resizeImage(IMG_WIDTH_SMALL, IMG_HEIGHT_SMALL, file_path, path+"/small.jpg");
        FileService.resizeImage(IMG_WIDTH_TINY, IMG_HEIGHT_TINY, file_path, path+"/tiny.jpg");        
    }
    
}
