package com.eduardocasas.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;
import javax.servlet.MultipartConfigElement;

/**
 * @author      Eduardo Casas <eduardocasas.com>
 * @version     1.0.0
 * @since       2014-03-16
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultiPartConfigFactory factory = new MultiPartConfigFactory();
        factory.setMaxFileSize("10000KB");
        factory.setMaxRequestSize("10000KB");

        return factory.createMultipartConfig();
    }
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
