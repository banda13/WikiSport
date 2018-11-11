package com.wikidata.sport.Model;

import java.util.*;

public class WikidataObject {

    private String title;

    private List<String> headers = new ArrayList<>();

    private List<List<Object>> rows = new ArrayList<>();

    public WikidataObject() {
    }

    public WikidataObject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<Object>> getRows() {
        return rows;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void setRows(List<List<Object>> rows) {
        this.rows = rows;
    }

    public void setUpFromEndpointResponse(HashMap response){
        HashMap result = (HashMap) response.get("result");
        headers = (ArrayList<String>) result.get("variables");
        for(HashMap<String, Object> row : (ArrayList<HashMap>) result.get("rows")){
            List<Object> values = new ArrayList<>();
            for(String header : headers){
                values.add(row.get(header));
            }
            rows.add(values);
        }
    }
}
