package com.eduardocasas.app.service;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
public class LogService {

    public static void insert(Exception e) throws Exception {
        System.setOut(new PrintStream(new FileOutputStream("src/main/webapp/WEB-INF/log/exception.log", true)));
        System.out.println(e);
    }
    
}
