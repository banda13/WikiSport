package com.wikidata.sport.Controllers;

import com.wikidata.sport.Model.Champions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String home(Model model) {
        List<Champions> winners = new ArrayList<>();
        winners.add(new Champions("1996-97", "Manchester United", "Newcastle United", "Liverpool"));
        winners.add(new Champions("1997-98", "Manchester United", "Newcastle United", "Arsenal"));
        winners.add(new Champions("1998-99", "Arsenal", "Manchester United", "Liverpool"));
        winners.add(new Champions("1999-2000", "Manchester United", "Arsenal", "\tChelsea"));
        model.addAttribute("winners", winners);
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
