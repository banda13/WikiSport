package com.wikidata.sport.Controllers;

import com.wikidata.sport.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@ComponentScan("")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @GetMapping("/hello")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        logger.info("Hello " + name);
        model.addAttribute("name", name);
        return "hello";
    }
}
