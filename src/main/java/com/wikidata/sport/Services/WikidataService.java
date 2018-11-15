package com.wikidata.sport.Services;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;
import com.wikidata.sport.Model.WikidataClientObjectType;
import com.wikidata.sport.Model.WikidataFormObject;
import com.wikidata.sport.Model.WikidataTableObject;
import org.apache.jena.base.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WikidataService {

    private static final Logger logger = LoggerFactory.getLogger(WikidataService.class);
    private static final String serviceUrl = "https://query.wikidata.org/sparql";

    public WikidataService(){
        logger.info("Wikidata service initialized");
    }

    public WikidataTableObject getPremierLeagueTeams() {
        try {
            logger.info("Getting premier league teams");
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataTableObject rs = new WikidataTableObject("Premier league teams");
            rs.setUpFromEndpointResponse(sp.query(SparqlQueries.getTeamsInPremierLeague));

            rs.setHeaders(Arrays.asList("Team", "Full name", "Headquarter", "Home venue"));
            rs.changeRowTypeForCustomLink(0, "/team?name=");

            logger.info(rs.getRows().size() + " teams found");
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    public WikidataTableObject getWinners(){
        try {
            logger.info("Getting winners");
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataTableObject rs = new WikidataTableObject("Champions");
            rs.setUpFromEndpointResponse(sp.query(SparqlQueries.getWinners));

            rs.setHeaders(Arrays.asList("Championship", "Winner", "Games played"));
            rs.changeRowTypeForCustomLink(1, "/team?name=");

            logger.info(rs.getRows().size() + " winners found");
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    public Map<String, String> getIdsForTeams(){
        try {
            logger.info("Resolving id's for teams");
            Map<String, String> result = new HashMap<>();
            Endpoint sp = new Endpoint(serviceUrl, false);
            HashMap response = (HashMap) sp.query(SparqlQueries.getIdsForTeams).get("result");
            List<HashMap> rows = (List<HashMap>) response.get("rows");
            for(HashMap row : rows){
                logger.info(row.get("teamLabel").toString() + " - " + row.get("team").toString());
                result.put(row.get("teamLabel").toString(), row.get("team").toString());
            }
            return result;
        } catch(EndpointException eex) {
            logger.error("Failed to get ids for teams", eex);
            return null;
        }
    }

    @Cacheable("team")
    public WikidataFormObject getDetailsForId(String name, String id){
        try {
            logger.info("Getting details for " + name + " with url " + id);
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataFormObject rs = new WikidataFormObject(name);
            rs.setUpFromEndpointResponse(sp.query(String.format(SparqlQueries.getTeamDetailsById, id)));

            rs.setHeaders(Arrays.asList("Inception", "Country", "Image", "Nickname", "Home venue", "Website", "Head coach", "Article", "League"));
            rs.changeRowType("Article", WikidataClientObjectType.LINK);
            rs.changeRowType("Website", WikidataClientObjectType.LINK);
            rs.changeRowType("Image", WikidataClientObjectType.IMAGE);
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get ids for teams", eex);
            return null;
        }
    }

    public static void main(String [] args){
        //getIdsForTeams();
    }

}
