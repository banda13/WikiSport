package com.wikidata.sport.Controllers;

import com.wikidata.sport.Model.Match;
import com.wikidata.sport.Model.WikidataFormObject;
import com.wikidata.sport.Model.WikidataObject;
import com.wikidata.sport.Model.WikidataTableObject;
import com.wikidata.sport.Services.WikidataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.EntityDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class DefaultController {

    private static final Logger logger = LoggerFactory.getLogger(WikidataService.class);

    @Autowired
    private WikidataService service;

    @GetMapping("/")
    public String home(Model model) {
        List<WikidataTableObject> response = new ArrayList<>();

        response.add(service.getPremierLeagueTeams());
        response.add(service.getWinners());
        response.add(service.getDiagonalScoreTable());
        response.add(service.getStandings());
        response.add(service.getMatchResults());
        model.addAttribute("response", response);
        return "/home";
    }

    @GetMapping("/home")
    public String home1(Model model) {
        return home(model);
    }

    @GetMapping("/teams")
    public String teams(Model model) {
        logger.info("Getting teams");
        List<WikidataFormObject> teams = new ArrayList<>();
        Map<String, String> teamMapping = service.getIdsForTeams();
        for (Map.Entry<String, String> entry : teamMapping.entrySet()) {
            teams.add(service.getDetailsForId(entry.getKey(), entry.getValue()));
        }
        model.addAttribute("teams", teams);
        return "/teams";
    }

    @GetMapping("/team")
    public String team(@RequestParam(name="name", required=true) String name, Model model){
        logger.info("Getting details for " + name);
        Map<String, String> teamMapping = service.getIdsForTeams();
        String teamId = teamMapping.get(name);
        model.addAttribute("team", service.getDetailsForId(name, teamId));
        return "/team";
    }

    @GetMapping("/insert")
    public String insert(Model model){
        model.addAttribute("match", new Match());
        model.addAttribute("teams", service.getIdsForTeams().keySet());
        return "/insert";
    }

    @PostMapping("/create")
    public String insert_post(@ModelAttribute Match newMatch, Model model) throws MediaWikiApiErrorException, IOException, LoginFailedException {
        logger.info("Creating new match started");
        Map<String, String> teamMapping = service.getIdsForTeams();
        newMatch.setTeam1Id(teamMapping.get(newMatch.getTeam1()));
        newMatch.setTeam2Id(teamMapping.get(newMatch.getTeam2()));

        newMatch = service.createNewMatch(newMatch);
        model.addAttribute("newMatch", newMatch);
        return "/insert_result";
    }

    @GetMapping("/seasons")
    public String seasons(){
        return "/seasons";
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
