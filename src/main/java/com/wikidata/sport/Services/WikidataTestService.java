package com.wikidata.sport.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WikidataTestService {

    private static final Logger logger = LoggerFactory.getLogger(WikidataTestService.class);

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

    public static void main(String args[]) throws MediaWikiApiErrorException {
        WikidataTestService service = new WikidataTestService();
        service.searchTest();
    }
}
