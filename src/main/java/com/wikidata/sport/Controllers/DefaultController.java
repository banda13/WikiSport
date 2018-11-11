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
        //TODO a servicet majd be kell injektálni
        List<WikidataObject> respone = new ArrayList<>();
        WikidataTestService test = new WikidataTestService();
        respone.add(test.getPremierLeagueTeams());
        respone.add(test.getWinners());
        model.addAttribute("response", respone);
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
