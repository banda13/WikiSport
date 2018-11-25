package com.wikidata.sport.Services;

import com.bordercloud.sparql.Endpoint;
import com.bordercloud.sparql.EndpointException;
import com.wikidata.sport.Model.Match;
import com.wikidata.sport.Model.WikidataClientObjectType;
import com.wikidata.sport.Model.WikidataFormObject;
import com.wikidata.sport.Model.WikidataTableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WikidataService {

    private static final Logger logger = LoggerFactory.getLogger(WikidataService.class);
    private static final String serviceUrl = "https://query.wikidata.org/sparql";
    private static final String siteIri = "http://www.wikidata.org/entity/";
    private static final Map<String,String> nameMap =
            Arrays.stream(new Object[][]{
                    {"Watford F.C.", "WAT"},
                    {"Everton F.C.", "EVE"},
                    {"Chelsea F.C.", "CHE"},
                    {"Arsenal F.C.", "ARS"},
                    {"Manchester United F.C.", "MUN"},
                    {"Middlesbrough F.C.", "MID"},
                    {"Cardiff City F.C.", "CAR"},
                    {"Newcastle United F.C.", "NEW"},
                    {"Southampton F.C.", "SOU"},
                    {"Stoke City F.C.", "STK"},
                    {"Tottenham Hotspur F.C.", "TOT"},
                    {"West Ham United F.C.", "WHU"},
                    {"Brighton & Hove Albion F.C.", "BRI"},
                    {"Burnley F.C.", "BUR"},
                    {"Crystal Palace F.C.", "CRY"},
                    {"Huddersfield Town A.F.C.", "HUD"},
                    {"Leicester City F.C.", "LEI"},
                    {"Wolverhampton Wanderers F.C.", "WOL"},
                    {"AFC Bournemouth", "BOU"},
                    {"Tranmere Rovers F.C.", "TRA"},
                    {"Manchester City F.C.", "MCI"},
                    {"Liverpool F.C.", "LIV"}
            }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));
    public static final String TEAM_NAME = "teamName";

    public WikidataService(){
        logger.info("Wikidata service initialized");
    }

    public static List<String> get2018_19Teams(){
        logger.info("Getting 2018-19 teams");
        try {
            Endpoint sp = new Endpoint(serviceUrl, false);
            HashMap hashMap = (HashMap)  sp.query(SparqlQueries.get2018_19Names).get("result");
            List<String> teamNames = new ArrayList<>();
            for (HashMap<String, Object> row :  (ArrayList<HashMap>) hashMap.get("rows")) {
                teamNames.add(row.get(TEAM_NAME).toString());
            }
            return teamNames;
        } catch (EndpointException e) {
            logger.error("Failed to get 2018-19 team names", e);
            return null;
        }
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
    public WikidataTableObject getDiagonalScoreTable(HashMap responseMatches) {
        try {
            logger.info("Getting teams");
            Endpoint sp = new Endpoint(serviceUrl, false);

            HashMap hashMap = sp.query(SparqlQueries.get2018_19Names);

            List<String> headers = getHeadersForDiagonalMatrix(hashMap,30);

            WikidataTableObject rs = new WikidataTableObject("2018-19 Premier league matches");

            HashMap extendedMapForScores = getExtendedScoreMap(hashMap,responseMatches);
            rs.setUpFromEndpointResponse(extendedMapForScores);

            rs.setHeaders(headers);
            rs.changeRowTypeForCustomLink(0, "/team?name=");

            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    private HashMap getExtendedScoreMap(HashMap hashMap, HashMap responseMatches) {
        HashMap result = (HashMap) hashMap.get("result");
        ArrayList<HashMap> resultRows = (ArrayList<HashMap>) result.get("rows");
        int countRows = resultRows.size();
        ArrayList<String> headers = (ArrayList<String>) result.get("variables");
        System.out.print("\n count: " + countRows);

        int index = -1;
        for (HashMap<String, Object> row : (ArrayList<HashMap>) result.get("rows")) {
            index++;
            String addable = nameMap.get(row.get(TEAM_NAME).toString());
            row.put(addable, "-");
            headers.add(addable);

            resultRows.set(index,row);
        }

        ((HashMap) hashMap.get("result")).put("rows",resultRows);


        for (HashMap<String, Object> row : (ArrayList<HashMap>) ((HashMap)hashMap.get("result")).get("rows")) {
            Map<String,String> cellFillMatch = new HashMap<>();

            for (HashMap<String, Object> response : (ArrayList<HashMap>) ((HashMap)responseMatches.get("result")).get("rows")) {

                List<String> participatingTeams = Arrays.asList(((String) response.get("teams")).split(","));
                if(participatingTeams.contains(row.get(TEAM_NAME).toString())){

                    String matchLabel = (String) response.get("matchLabel");

                    String matchDescription = (String) response.get("matchDescription");
                    if(!matchLabel.startsWith(row.get(TEAM_NAME).toString())){
                        matchDescription = new StringBuilder(matchDescription).reverse().toString();
                    }

                    String otherTeam = null;
                    for (String var: participatingTeams) {
                        if(!var.equals(row.get(TEAM_NAME).toString())){
                            otherTeam = var;

                        }
                    }
                    cellFillMatch.put(otherTeam,matchDescription);
                }
            }
            if(!cellFillMatch.isEmpty()) {
                for (int i = 1; i <= countRows; i++) {
                    String head = headers.get(i);
                    String origin = getKeyFromValue(nameMap, head);
                    if(cellFillMatch.containsKey(origin)){
                        row.put(head,cellFillMatch.get(origin));
                    }
                }
            }
        }
        return hashMap;
    }

    public static String getKeyFromValue(Map<String,String> hm, String value) {
        for (String o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }


    public List<String> getHeadersForDiagonalMatrix(HashMap rs , int size) {

        List<String> headers = new ArrayList<>();
        headers.add("Home \\ Away");
        System.out.print("\n");
        HashMap result = (HashMap) rs.get("result");
        List<String> variables = (ArrayList<String>) result.get("variables");
        for (String variable : variables) {
            System.out.print(String.format("%-" + size + "." + size + "s", variable) + " | ");
        }
        System.out.print("\n");
        for (HashMap<String, Object> row : (ArrayList<HashMap>) result.get("rows")) {

            for (String variable : variables) {

                if(variable.equals(TEAM_NAME)) {
                    System.out.print(String.format("%-" + size + "." + size + "s", row.get(variable)) + " | ");
                    String addable = nameMap.get(row.get(variable).toString());
                    headers.add(addable);
                }
            }
            System.out.print("\n");
        }

        System.out.print("\n headerscount: " + headers.size());
        return headers;
    }

    public WikidataTableObject getStandings(HashMap responseMatches) {
        try {
            logger.info("Getting teams");
            Endpoint sp = new Endpoint(serviceUrl, false);

            HashMap hashMap = sp.query(SparqlQueries.getStandings);

            WikidataTableObject rs = new WikidataTableObject("2018-19 Premier league standing");

            HashMap extendedMapForScores = getStandingTableMap(hashMap,responseMatches);
            rs.setUpFromEndpointResponse(extendedMapForScores);

            rs.setHeaders(Arrays.asList("Place", "Team", "Match", "W", "D", "L", "Points", "Description"));
            rs.changeRowTypeForCustomLink(1, "/team?name=");

            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    private HashMap getStandingTableMap(HashMap hashMap, HashMap responseMatches) {
        HashMap result = (HashMap) hashMap.get("result");
        ArrayList<String> headers = (ArrayList<String>) result.get("variables");

        for (HashMap<String, Object> row : (ArrayList<HashMap>) ((HashMap)hashMap.get("result")).get("rows")) {

            int allGames=0;
            int win = 0;
            int lose = 0;
            int draw = 0;
            int points = 0;
            for (HashMap<String, Object> response : (ArrayList<HashMap>) ((HashMap)responseMatches.get("result")).get("rows")) {

                List<String> participatingTeams = Arrays.asList(((String) response.get("teams")).split(","));
                if(participatingTeams.contains(row.get(TEAM_NAME).toString())){
                    allGames++;

                    String nyertesLabel = (String) response.get("nyertesLabel");

                    if(nyertesLabel.equals(row.get(TEAM_NAME).toString())){
                        win++;
                    } else if (nyertesLabel.equals("draw")){
                        draw++;
                    } else {
                        lose ++;
                    }
                }
            }
            points = win*3 + draw;
            row.put("matchPlayed",Integer.toString(allGames));
            row.put("win",Integer.toString(win));
            row.put("draw",Integer.toString(draw));
            row.put("lose",Integer.toString(lose));
            row.put("points",points);

        }

        Collections.sort((ArrayList<HashMap<String, Object>>)  ((HashMap)hashMap.get("result")).get("rows"), new Comparator<HashMap<String, Object>>(){
            public int compare(HashMap<String, Object> one, HashMap<String, Object> two) {
                return one.get("points").toString().compareTo(two.get("points").toString());
            }
        });

        int countRows = ((ArrayList<HashMap>) result.get("rows")).size();
        int index = 0;
        for (HashMap<String, Object> row : (ArrayList<HashMap>) result.get("rows")) {
            index++;

            for (String variable : headers) {
                if(variable.equals("place")) {
                    row.put("place",Integer.toString(index));
                }
                if(variable.equals("description")){
                    if(index >=1 && index <=4)
                        row.put("description", "Bajnokok Ligája-csoportkör");
                    if(index==5)
                        row.put("description", "Európa-liga-csoportkör");
                    if(index>countRows-3)
                        row.put("description", "Kiesik az EFL Championshipbe");
                }
            }
        }
        return hashMap;
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

    public WikidataTableObject getMatchResults(){
        try {
            logger.info("Getting inserted match results");
            Endpoint sp = new Endpoint(serviceUrl, false);
            WikidataTableObject rs = new WikidataTableObject("Match results");
            rs.setUpFromEndpointResponse(sp.query(SparqlQueries.getMatchResults));

            rs.setHeaders(Arrays.asList("Match", "Result", "Winner", "Participating teams"));
            return rs;
        } catch(EndpointException eex) {
            logger.error("Failed to get premier league teams", eex);
            return null;
        }
    }

    @Cacheable("ids")
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

    public Match createNewMatch(Match match) throws MediaWikiApiErrorException, LoginFailedException, IOException {
        logger.info("Creating new match: " + match.getTeam1() + " vs " + match.getTeam2());

        ItemIdValue noid = ItemIdValue.NULL;
        WikibaseDataFetcher wbdf = WikibaseDataFetcher.getWikidataDataFetcher();
        PropertyIdValue instanceOfStatement = ((PropertyDocument) wbdf.getEntityDocument("P31")).getEntityId();
        PropertyIdValue partOfStatement = ((PropertyDocument) wbdf.getEntityDocument("P361")).getEntityId();
        PropertyIdValue participant1Statement = ((PropertyDocument) wbdf.getEntityDocument("P710")).getEntityId();
        PropertyIdValue participant2Statement = ((PropertyDocument) wbdf.getEntityDocument("P710")).getEntityId();

        PropertyIdValue winnerStatement = ((PropertyDocument) wbdf.getEntityDocument("P1346")).getEntityId();

        Statement statement1 = StatementBuilder
                .forSubjectAndProperty(noid, instanceOfStatement)
                .withValue(Datamodel.makeWikidataItemIdValue(WikidataConsts.ASSOSIATIONFOOTBALLMATCH)).build();
        Statement statement2 = StatementBuilder
                .forSubjectAndProperty(noid, partOfStatement)
                .withValue(Datamodel.makeWikidataItemIdValue(WikidataConsts.PERIOD18_19)).build();

        Statement statement3 = StatementBuilder
                .forSubjectAndProperty(noid, participant1Statement)
                .withValue(Datamodel.makeWikidataItemIdValue(match.getTeam1Id())).build();
        Statement statement4 = StatementBuilder
                .forSubjectAndProperty(noid, participant2Statement)
                .withValue(Datamodel.makeWikidataItemIdValue(match.getTeam2Id())).build();
        Statement statement5 = StatementBuilder
                .forSubjectAndProperty(noid, winnerStatement)
                .withValue(Datamodel.makeWikidataItemIdValue(match.getWinnerId())).build();

        String label = match.getTeam1() + " vs " + match.getTeam2();
        String description = match.getTeam1Goals() + "-" + match.getTeam2Goals();

        logger.info("Logging into wikidata");
        ApiConnection connection = ApiConnection.getWikidataApiConnection();
        connection.login("szabag", "wikipass111"); //secret

        WikibaseDataEditor wbde = new WikibaseDataEditor(connection, siteIri);
        ItemDocument itemDocument = ItemDocumentBuilder.forItemId(noid)
                .withLabel(label, "en")
                .withDescription(description, "en")
                .withStatement(statement1)
                .withStatement(statement2)
                .withStatement(statement3)
                .withStatement(statement4)
                .withStatement(statement5)
                .build();

        logger.info("Inserting new itemdocument via wikitoolkit");
        ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                "Wikisport data generation");

        ItemIdValue newItemId = newItemDocument.getEntityId();
        logger.info("New item created with id: " + newItemId.getId());
        match.setWikidataId(newItemId.getId());
        return match;
    }

}
