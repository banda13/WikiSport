package com.wikidata.sport.Services;

class SparqlQueries {

    public static final String getTeamsInPremierLeague = "SELECT DISTINCT ?teamLabel ?hivatalos_n_v ?sz_khelyLabel ?hazai_stadion__sz_khely_Label WHERE {\n" +
            "  ?team (wdt:P118/wdt:P279*) wd:Q9448.\n" +
            "  ?team (wdt:P31/wdt:P279*) wd:Q476028.\n" +
            "  ?team wdt:P17 wd:Q145.\n" +
            "  OPTIONAL { ?team wdt:P571 ?inception. }\n" +
            "  OPTIONAL { ?team wdt:P1448 ?hivatalos_n_v. }\n" +
            "  OPTIONAL { ?team wdt:P159 ?sz_khely. }\n" +
            "  OPTIONAL { ?team wdt:P115 ?hazai_stadion__sz_khely_. }\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "}\n" +
            "ORDER BY ?Name";

    public static final String getStandings = "SELECT DISTINCT (1 as ?place) ?teamName (0 as ?matchPlayed) (0 as ?win) (0 as ?draw) (0 as ?lose) (0 as ?points) ('' as ?description) WHERE {\n" +
            "  ?team wdt:P3450 wd:Q9448.\n" +
            "  ?team wdt:P31 wd:Q27020041.\n" +
            "  ?team wdt:P17 wd:Q145.\n" +
            "  ?team wdt:P2348 wd:Q52394608.\n" +
            "  ?team wdt:P1923 ?participating.\n" +
            "  OPTIONAL { ?participating rdfs:label ?teamName.\n" +
            "             FILTER(LANG(?teamName) = \"en\").}\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "}\n" +
            "ORDER BY ?teamName";

    public static final String get2018_19Names = "SELECT DISTINCT ?teamName WHERE {\n" +
            "  ?team wdt:P3450 wd:Q9448.\n" +
            "  ?team wdt:P31 wd:Q27020041.\n" +
            "  ?team wdt:P17 wd:Q145.\n" +
            "  ?team wdt:P2348 wd:Q52394608.\n" +
            "  ?team wdt:P1923 ?participating.\n" +
            "  OPTIONAL { ?participating rdfs:label ?teamName.\n" +
            "             FILTER(LANG(?teamName) = \"en\").}\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "}\n" +
            "ORDER BY ?teamName";

    public static final String getWinners = "SELECT DISTINCT ?leagueLabel ?nyertesLabel ?merkozesek_szama WHERE {\n" +
            "  ?league wdt:P3450 wd:Q9448.\n" +
            "  ?league wdt:P1346 ?nyertes.\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "  OPTIONAL { ?league wdt:P1350 ?merkozesek_szama. }\n" +
            "}\n" +
            "ORDER BY ?korszakLabel";

    public static final String getIdsForTeams = "SELECT DISTINCT ?teamLabel ?team  WHERE {\n" +
            "  ?team (wdt:P118/wdt:P279*) wd:Q9448.\n" +
            "  ?team (wdt:P31/wdt:P279*) wd:Q476028.\n" +
            "  ?team wdt:P17 wd:Q145.\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "}";

    public static String getTeamDetailsById = "SELECT DISTINCT ?article ?countryLabel ?imageLabel ?inceptionLabel ?nicknameLabel ?headcoachLabel ?leagueLabel ?homevenueLabel ?officalwebsiteLabel\n" +
            "WHERE\n" +
            "  { ?article  schema:about       ?item ;\n" +
            "              schema:inLanguage  \"en\" ;\n" +
            "              schema:isPartOf    <https://en.wikipedia.org/> .\n" +
            "    OPTIONAL { ?item wdt:P17 ?country. }\n" +
            "    OPTIONAL { ?item wdt:P18 ?image. }\n" +
            "    OPTIONAL { ?item wdt:P571 ?inception. }\n" +
            "    OPTIONAL { ?item wdt:P1449 ?nickname. }\n" +
            "    OPTIONAL { ?item wdt:P286 ?headcoach. }\n" +
            "    OPTIONAL { ?item wdt:P118 ?league. }\n" +
            "    OPTIONAL { ?item wdt:P115 ?homevenue. }\n" +
            "    OPTIONAL { ?item wdt:P856 ?officalwebsite. }\n" +
            "    FILTER ( ?item = <%s> )    \n" +
            "    SERVICE wikibase:label\n" +
            "      { bd:serviceParam\n" +
            "                  wikibase:language  \"en\"\n" +
            "      }\n" +
            "  }\n" +
            "";

    public static String getMatchResults = "SELECT ?matchLabel ?matchDescription ?nyertesLabel WHERE {\n" +
            "  ?match (wdt:P31/wdt:P279*) wd:Q16466010.\n" +
            "  ?match wdt:P361 wd:Q9448.\n" +
            "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n" +
            "  OPTIONAL { ?match wdt:P1346 ?nyertes. }\n" +
            "}\n" +
            "ORDER BY ?match";
}
