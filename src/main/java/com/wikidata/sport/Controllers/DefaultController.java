package com.wikidata.sport.Controllers;

import com.wikidata.sport.Model.Champions;
import com.wikidata.sport.Model.WikidataObject;
import com.wikidata.sport.Services.WikidataTestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String home(Model model) {
        //TODO ez majd be kell injektálni
        WikidataTestService test = new WikidataTestService();
        model.addAttribute("teams", test.getPremierLeagueTeams());
        return "/home";
    }

    @GetMapping("/home")
    public String home1(Model model) {
        return home(model);
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }


}
