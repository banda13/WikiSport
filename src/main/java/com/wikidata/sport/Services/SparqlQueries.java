package com.wikidata.sport.Services;

public class SparqlQueries {

    public static final String getTeamsInPremierLeague = "SELECT DISTINCT ?teamLabel ?hivatalos_n_v ?inception ?sz_khelyLabel ?hazai_stadion__sz_khely_Label WHERE {\n" +
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
}
