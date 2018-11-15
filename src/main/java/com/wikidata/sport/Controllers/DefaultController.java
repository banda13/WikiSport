package com.wikidata.sport.Controllers;

import com.wikidata.sport.Model.WikidataTableObject;
import com.wikidata.sport.Services.WikidataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    @Autowired
    private WikidataService service;

    @GetMapping("/")
    public String home(Model model) {
        List<WikidataTableObject> respone = new ArrayList<>();
        respone.add(service.getPremierLeagueTeams());
        respone.add(service.getWinners());
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
        Map<String, String> teamMapping = service.getIdsForTeams();
        String teamId = teamMapping.get(name);
        model.addAttribute("team", service.getDetailsForId(name, teamId));
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
