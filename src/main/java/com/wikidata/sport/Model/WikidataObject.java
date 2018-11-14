package com.wikidata.sport.Model;

import java.util.*;

public class WikidataObject {

    private String title;

    private List<String> headers = new ArrayList<>();

    private List<List<WikidataRowObject>> rows = new ArrayList<>();

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


    public void setTitle(String title) {
        this.title = title;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<WikidataRowObject>> getRows() {
        return rows;
    }

    public void setRows(List<List<WikidataRowObject>> rows) {
        this.rows = rows;
    }

    public void changeRowType(int rowIndex, WikidataClientObjectType newType){
        for(List<WikidataRowObject> row : rows){
            for(int i = 0; i < row.size(); i++){
                if(i == rowIndex){
                    row.get(i).setType(newType);
                }
            }
        }
    }

    public void changeRowTypeForCustomLink(int rowIndex, String link){
        for(List<WikidataRowObject> row : rows){
            for(int i = 0; i < row.size(); i++){
                if(i == rowIndex){
                    row.get(i).setType(WikidataClientObjectType.LINK);
                    String linkUrl = link + row.get(i).getValue().toString();
                    row.get(i).setLinkInfo(linkUrl);
                }
            }
        }
    }

    public void setUpFromEndpointResponse(HashMap response){
        HashMap result = (HashMap) response.get("result");
        headers = (ArrayList<String>) result.get("variables");
        for(HashMap<String, Object> row : (ArrayList<HashMap>) result.get("rows")){
            List<WikidataRowObject> values = new ArrayList<>();
            for(String header : headers){
                values.add(new WikidataRowObject(row.get(header), WikidataClientObjectType.TEXT));
            }
            rows.add(values);
        }
    }
}
