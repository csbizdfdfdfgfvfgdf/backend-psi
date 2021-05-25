package com.notepad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The NetworkNotepadApplication program implements an application that
 * simply starts the complete spring boot application.
 *
 * @author Zohaib Ali
 * @version 1.0
 * @since 2021-04-22
 */
@SpringBootApplication
public class NetworkNotepadApplication extends SpringBootServletInitializer {

    private static Class applicationClass = NetworkNotepadApplication.class;

    public static void main(String[] args) {
        SpringApplication.run(applicationClass, args);
    }

}
