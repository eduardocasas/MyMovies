package com.eduardocasas.app.controller;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import java.io.File;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-05-23
 */
@Controller
public class SessionController {

    private String USER_NAME;
    private String USER_PASSWORD;
    
    public SessionController() throws Exception {
        setParameters();        
    }
    
    @RequestMapping("/session/logout")
    public String logout(HttpSession session) throws Exception {
        session.setAttribute("login", false);
        return "redirect:/session/login";
    }
    
    @RequestMapping("/session/login")
    public String login(HttpSession session) throws Exception {
        if (session.getAttribute("login") != null && (Boolean)session.getAttribute("login")) {
            return "redirect:/";
        }
        return "session/login";
    }
    
    @RequestMapping("/session/login_process")
    public String loginProcess(
        HttpSession session,
        @RequestParam("username") String username,
        @RequestParam("password") String password
    ) throws Exception {
        if (new String(USER_NAME).equals(username) && new String(USER_PASSWORD).equals(password)) {
            session.setAttribute("login", true);
            return "redirect:/";
        } else {
            return "redirect:/session/login";
        }
    }
    
    private void setParameters() throws Exception {
        Ini ini = new Ini(new File("src/main/resources/parameters.ini"));
        Section section = ini.get("security");
        USER_NAME = section.get("USER_NAME");
        USER_PASSWORD = section.get("USER_PASSWORD");
    }
    
}
