package com.wikidata.sport.Controllers;

import com.wikidata.sport.Model.WikidataObject;
import com.wikidata.sport.Services.WikidataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String home(Model model) {
        //TODO a servicet majd be kell injektálni
        List<WikidataObject> respone = new ArrayList<>();
        WikidataService test = new WikidataService();
        respone.add(test.getPremierLeagueTeams());
        respone.add(test.getWinners());
        model.addAttribute("response", respone);
        return "/home";
    }

    @GetMapping("/home")
    public String home1(Model model) {
        return home(model);
    }

    @GetMapping("/teams")
    public String teams(Model model){
        return "/teams";
    }

    @GetMapping("/team")
    public String team(@RequestParam(name="name", required=true) String name, Model model){
        WikidataService test = new WikidataService();
        Map<String, String> teamMapping = test.getIdsForTeams();
        model.addAttribute("team", teamMapping.get(name));
        return "/team";
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
