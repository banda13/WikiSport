package com.wikidata.sport.Services;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;
import com.wikidata.sport.Model.WikidataClientObjectType;
import com.wikidata.sport.Model.WikidataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.util.*;


public class WikidataService {

    private static final Logger logger = LoggerFactory.getLogger(WikidataService.class);
    private static final String serviceUrl = "https://query.wikidata.org/sparql";

    public WikidataObject getPremierLeagueTeams() {
        try {
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataObject rs = new WikidataObject("Premier league teams");
            rs.setUpFromEndpointResponse(sp.query(SparqlQueries.getTeamsInPremierLeague));

            rs.setHeaders(Arrays.asList("Team", "Full name", "Inception", "Headquarter", "Home venue"));
            rs.changeRowType(2, WikidataClientObjectType.TIME);
            rs.changeRowTypeForCustomLink(0, "/team?name=");
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    public WikidataObject getWinners(){
        try {
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataObject rs = new WikidataObject("Champions");
            rs.setUpFromEndpointResponse(sp.query(SparqlQueries.getWinners));

            rs.setHeaders(Arrays.asList("Championship", "Winner", "Games played"));
            rs.changeRowTypeForCustomLink(1, "/team?name=");
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }



    public static void main(String [] args){
        //getPremierLeagueTeams();
    }

}
