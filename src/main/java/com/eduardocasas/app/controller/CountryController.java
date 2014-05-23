package com.eduardocasas.app.controller;

import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.service.FileService;
import com.eduardocasas.app.model.PersonModel;
import com.eduardocasas.app.model.MovieModel;
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
 * @since       2014-05-18
 */
@Controller
public class CountryController {
    
    private static final String PICTURES_PATH = "src/main/webapp/pictures/country/";
    
    private static final int IMG_WIDTH_BIG     = 240;
    private static final int IMG_HEIGHT_BIG    = 120;
    private static final int IMG_WIDTH_MEDIUM  = 120;
    private static final int IMG_HEIGHT_MEDIUM = 60;
    private static final int IMG_WIDTH_SMALL   = 60;
    private static final int IMG_HEIGHT_SMALL  = 30;
    private static final int IMG_WIDTH_TINY    = 30;
    private static final int IMG_HEIGHT_TINY   = 15;

    @RequestMapping("/countries")
    public String index(Model model) throws Exception {
        model.addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()));

        return "country/index";
    }
    
    @RequestMapping("/countries/{id}")
    public String enter(@PathVariable int id, Model model) throws Exception {
        model.addAttribute("country", ResponseService.getItem(new CountryModel().getItem(id)))
             .addAttribute("countryPeople", ResponseService.getList(new PersonModel().getCollectionByCountry(id)))
             .addAttribute("countryMovies", ResponseService.getList(new MovieModel().getCollectionByCountry(id)));

        return "country/info";
    }
    
    @RequestMapping("/countries/add")
    public @ResponseBody int add(
        @RequestParam("name") String name
    ) throws Exception {
        return new CountryModel().insert(name);
    }
    
    @RequestMapping("/countries/{id}/edit")
    public @ResponseBody void edit(
        @PathVariable int id,
        @RequestParam("name") String name
    ) throws Exception {
        new CountryModel().edit(id, name);
    }

    @RequestMapping("/countries/{id}/remove")
    public @ResponseBody void remove(@PathVariable int id) throws Exception {
        new CountryModel().delete(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    @RequestMapping(value = "/countries/{id}/upload-picture",  headers = "content-type=multipart/*")
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
            new CountryModel().setPictureActive(id);
        }
    }
    
    @RequestMapping("/countries/{id}/remove-picture")
    public @ResponseBody void removePicture(@PathVariable int id) throws Exception {
        new CountryModel().setPictureNull(id);
        FileService.removeFolder(PICTURES_PATH+Integer.toString(id));
    }
    
    private static void createMultipleImages(String file_path, String folder_path) {
        FileService.resizeImage(IMG_WIDTH_BIG, IMG_HEIGHT_BIG, file_path, folder_path+"/big.jpg");
        FileService.resizeImage(IMG_WIDTH_MEDIUM, IMG_HEIGHT_MEDIUM, file_path, folder_path+"/medium.jpg");
        FileService.resizeImage(IMG_WIDTH_SMALL, IMG_HEIGHT_SMALL, file_path, folder_path+"/small.jpg");
        FileService.resizeImage(IMG_WIDTH_TINY, IMG_HEIGHT_TINY, file_path, folder_path+"/tiny.jpg");        
    }
    
}
