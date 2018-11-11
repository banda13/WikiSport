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
}
