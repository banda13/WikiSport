package com.wikidata.sport.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyErrorController {

    private static final String PATH = "/error";

    @GetMapping("/error")
    public String error() {
        return "/error";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "/error/accessDenied";
    }

}
