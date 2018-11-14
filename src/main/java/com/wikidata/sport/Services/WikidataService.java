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
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    public static void main(String [] args){
        //getPremierLeagueTeams();
    }

    public void searchTest() throws MediaWikiApiErrorException {
        WikibaseDataFetcher wbdf = WikibaseDataFetcher.getWikidataDataFetcher();
        List<WbSearchEntitiesResult> results = wbdf.searchEntities("premier league");

        for (WbSearchEntitiesResult result : results) {
            logger.info(result.getTitle() + " " + result.getLabel());
        }

        EntityDocument q9448 = wbdf.getEntityDocument("Q9448");
        if (q9448 instanceof ItemDocument) {
            logger.info("The English name for entity Q9448 is "
                    + ((ItemDocument) q9448).getLabels().get("en").getText());

            Map<String, SiteLink> q998SiteLinks = ((ItemDocument) q9448).getSiteLinks();

            for (Iterator<Statement> it = ((ItemDocument) q9448).getAllStatements(); it.hasNext(); ) {
                Statement st = it.next();
                logger.info(st.getClaim().getSubject().getSiteIri());
                PropertyIdValue propQ4448 = st.getMainSnak().getPropertyId();
                logger.info(propQ4448.getSiteIri() + propQ4448);
            }
        }
    }
}
