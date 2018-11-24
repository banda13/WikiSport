package com.wikidata.sport.Model;

import java.util.*;

public class WikidataTableObject {

    private String title;

    private List<String> headers = new ArrayList<>();

    public List<String> getRowHeaders() {
        return rowHeaders;
    }

    public void setRowHeaders(List<String> rowHeaders) {
        this.rowHeaders = rowHeaders;
    }

    private List<String> rowHeaders = new ArrayList<>();

    private List<List<WikidataObject>> rows = new ArrayList<>();

    public WikidataTableObject() {
    }

    public WikidataTableObject(String title) {
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

    public List<List<WikidataObject>> getRows() {
        return rows;
    }

    public void setRows(List<List<WikidataObject>> rows) {
        this.rows = rows;
    }

    public void changeRowType(int rowIndex, WikidataClientObjectType newType){
        for(List<WikidataObject> row : rows){
            for(int i = 0; i < row.size(); i++){
                if(i == rowIndex){
                    row.get(i).setType(newType);
                    row.get(i).setLinkInfo(row.get(i).getValue().toString());
                }
            }
        }
    }

    public void changeRowTypeForCustomLink(int rowIndex, String link){
        for(List<WikidataObject> row : rows){
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
            List<WikidataObject> values = new ArrayList<>();
            for(String header : headers){
                values.add(new WikidataObject(row.get(header), WikidataClientObjectType.TEXT));
            }
            rows.add(values);
        }
    }


}
