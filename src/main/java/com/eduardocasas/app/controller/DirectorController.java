package com.eduardocasas.app.controller;

import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.model.CountryModel;
import com.eduardocasas.app.model.PersonModel;
import com.eduardocasas.app.model.DirectorModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.ResultSet;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
@Controller
public class DirectorController {

    @RequestMapping("/directors")
    public String index(Model model) throws Exception {
        ResultSet directors = new DirectorModel().getCollection();
        model.addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()))
             .addAttribute("people", ResponseService.getList(new PersonModel().getCollection(directors)))
             .addAttribute("directors", ResponseService.getList(directors));

        return "director/index";
    }
    
    @RequestMapping("/directors/add")
    public @ResponseBody void add(@RequestParam("person_id") String person_id) throws Exception {
        new DirectorModel().insert(Integer.parseInt(person_id));
    }
    
    @RequestMapping("/directors/addnew")
    public @ResponseBody int addnew(
        @RequestParam("name") String name,
        @RequestParam("surname") String surname,
        @RequestParam("birthday") String birthday,
        @RequestParam("country") String country
    ) throws Exception {
        int person_id = new PersonModel().insert(name, surname, Integer.parseInt(birthday), Integer.parseInt(country));
        new DirectorModel().insert(person_id);
                
        return person_id;
    }

}
