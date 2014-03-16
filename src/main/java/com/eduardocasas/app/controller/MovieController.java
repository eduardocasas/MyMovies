package com.eduardocasas.app.controller;

import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.service.FileService;
import com.eduardocasas.app.model.MovieModel;
import com.eduardocasas.app.model.ActorModel;
import com.eduardocasas.app.model.DirectorModel;
import com.eduardocasas.app.model.CountryModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
@Controller
public class MovieController {
    
    private static final String PICTURES_PATH = "src/main/webapp/pictures/movie/";
    
    private static final int IMG_WIDTH_BIG     = 180;
    private static final int IMG_HEIGHT_BIG    = 250;
    private static final int IMG_WIDTH_MEDIUM  = 180;
    private static final int IMG_HEIGHT_MEDIUM = 130;
    private static final int IMG_WIDTH_SMALL   = 100;
    private static final int IMG_HEIGHT_SMALL  = 100;
    private static final int IMG_WIDTH_TINY    = 60;
    private static final int IMG_HEIGHT_TINY   = 60;

    @RequestMapping("/movies")
    public String index(Model model) throws Exception {
        model.addAttribute("movies", ResponseService.getList(new MovieModel().getCollection()))
             .addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()));

        return "movie/index";
    }
    
    @RequestMapping("/movies/{id}")
    public String enter(@PathVariable int id, Model model) throws Exception {
        ActorModel Actor = new ActorModel();
        DirectorModel Director = new DirectorModel();
        model.addAttribute("movie", ResponseService.getItem(new MovieModel().getItem(id)))
             .addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()))
             .addAttribute("actors", ResponseService.getList(Actor.getCollection()))
             .addAttribute("directors", ResponseService.getList(Director.getCollection()))
             .addAttribute("movieDirectors", ResponseService.getList(Director.getCollection(id)))
             .addAttribute("movieActors", ResponseService.getList(Actor.getCollection(id)));

        return "movie/info";
    }
    
    @RequestMapping("/movies/add")
    public @ResponseBody int add(
        @RequestParam("title") String title,
        @RequestParam("year") String year,
        @RequestParam("score") String score,
        @RequestParam("country") String country
    ) throws Exception {
        return new MovieModel().insert(title, Integer.parseInt(year), Float.parseFloat(score), Integer.parseInt(country));
    }
    
    @RequestMapping("/movies/{id}/edit")
    public @ResponseBody void edit(
        @PathVariable int id,
        @RequestParam("title") String title,
        @RequestParam("score") String score,
        @RequestParam("year") String year,
        @RequestParam("country") String country,
        @RequestParam(value="actor_movie[]", required=false) String[] actor_movie,
        @RequestParam(value="director_movie[]", required=false) String[] director_movie
    ) throws Exception {
        new MovieModel().edit(id, title, Integer.parseInt(year), Float.parseFloat(score), Integer.parseInt(country));
        new ActorModel().editMovies(actor_movie, id);
        new DirectorModel().editMovies(director_movie, id);
    }

    @RequestMapping("/movies/{id}/remove")
    public @ResponseBody void remove(@PathVariable int id) throws Exception {
        new MovieModel().delete(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    @RequestMapping(value = "/movies/{id}/upload-picture",  headers = "content-type=multipart/*")
    public @ResponseBody void uploadPicture(
        @PathVariable int id,
        @RequestParam("upload_picture") MultipartFile file
    ) throws Exception {
        if (!file.isEmpty()) {
            String folder_path = PICTURES_PATH+Integer.toString(id);
            String file_name = file.getOriginalFilename();
            String file_path = folder_path+"/"+file_name;
            FileService.uploadFile(file, folder_path, file_name);
            FileService.convertImageToJpg(file_path);
            createMultipleImages(file_path, folder_path);
            FileService.removeFile(file_path);
            new MovieModel().setPictureActive(id);
        }
    }
    
    @RequestMapping("/movies/{id}/remove-picture")
    public @ResponseBody void removePicture(@PathVariable int id) throws Exception {
        new MovieModel().setPictureNull(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    private static void createMultipleImages(String file_path, String folder_path) {
        FileService.resizeImage(IMG_WIDTH_BIG, IMG_HEIGHT_BIG, file_path, folder_path+"/big.jpg");
        FileService.resizeImage(IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, file_path, folder_path+"/medium.jpg");
        FileService.resizeImage(IMG_WIDTH_SMALL, IMG_HEIGHT_SMALL, file_path, folder_path+"/small.jpg");
        FileService.resizeImage(IMG_WIDTH_TINY, IMG_HEIGHT_TINY, file_path, folder_path+"/tiny.jpg");        
    }
    
}
