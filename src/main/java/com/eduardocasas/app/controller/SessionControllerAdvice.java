package com.eduardocasas.app.controller;

import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-05-23
 */
@ControllerAdvice
public class SessionControllerAdvice {

    private HttpSession session;
    private HttpServletRequest request;

    @ModelAttribute
    public void  myMethod(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.session = session;
        this.request = request;
        if (userIsNotAuthorized()) {
            response.sendRedirect("/session/login");
        }
    }

    private Boolean userIsNotAuthorized() {
        return (session.getAttribute("login") == null || !(Boolean)session.getAttribute("login")) && !request.getRequestURI().contains("session");
    }

}