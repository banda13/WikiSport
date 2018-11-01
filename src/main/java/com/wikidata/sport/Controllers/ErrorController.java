package com.wikidata.sport.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String error() {
        return "/error/error";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "/error/accessDenied";
    }
}
