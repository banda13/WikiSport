package com.wikidata.sport.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/login")
    public String login() {
        return "authentication/login";
    }

    @GetMapping("/signin")
    public String signIn() {
        return "authentication/signin";
    }
}
