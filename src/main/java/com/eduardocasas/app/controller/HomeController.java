package com.eduardocasas.app.controller;

import com.eduardocasas.app.service.ResponseService;
import com.eduardocasas.app.model.MovieModel;
import com.eduardocasas.app.model.PersonModel;
import com.eduardocasas.app.model.ActorModel;
import com.eduardocasas.app.model.DirectorModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
@Controller
public class HomeController {

    @RequestMapping("")
    public String index(Model model) throws Exception {
        MovieModel Movie = new MovieModel();
        model.addAttribute("movies", ResponseService.getList(Movie.getLastItemsCollection()))
             .addAttribute("people", ResponseService.getList(new PersonModel().getLastItemsCollection()))
             .addAttribute("totalMovies", Movie.getTotalNum())
             .addAttribute("totalDirectors", new DirectorModel().getTotalNum())
             .addAttribute("actorActors", new ActorModel().getTotalNum());

        return "home";
    }

}
