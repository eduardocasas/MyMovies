package com.eduardocasas.app.controller;

import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.model.CountryModel;
import com.eduardocasas.app.model.PersonModel;
import com.eduardocasas.app.model.ActorModel;
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
public class ActorController {

    @RequestMapping("/cast")
    public String index(Model model) throws Exception {
        ActorModel Actor = new ActorModel();
        ResultSet actors = Actor.getCollection();
        model.addAttribute("countries", ResponseService.getList(new CountryModel().getCollection()))
             .addAttribute("people", ResponseService.getList(new PersonModel().getCollection(actors)))
             .addAttribute("actors", ResponseService.getList(actors));

        return "actor/index";
    }
    
    @RequestMapping("/cast/add")
    public @ResponseBody void add(@RequestParam("person_id") String person_id) throws Exception {
        new ActorModel().insert(Integer.parseInt(person_id));
    }
    
    @RequestMapping("/cast/addnew")
    public @ResponseBody int addnew(
        @RequestParam("name") String name,
        @RequestParam("surname") String surname,
        @RequestParam("birthday") String birthday,
        @RequestParam("country") String country
    ) throws Exception {
        int person_id = new PersonModel().insert(name, surname, Integer.parseInt(birthday), Integer.parseInt(country));
        new ActorModel().insert(person_id);
                
        return person_id;
    }

}
