package com.wikidata.sport.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikidataFormObject {

    private String title;

    private Map<String, WikidataObject> fields = new HashMap<>();

    public WikidataFormObject() {
    }

    public WikidataFormObject(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, WikidataObject> getFields() {
        return fields;
    }

    public void setFields(Map<String, WikidataObject> fields) {
        this.fields = fields;
    }

    public void setHeaders(List<String> headers){
        Map<String, WikidataObject> newFields = new HashMap<>();
        int column = 0;
        for (Map.Entry<String, WikidataObject> entry : fields.entrySet())
        {
            newFields.put(headers.get(column), entry.getValue());
            column += 1;
        }
        fields = newFields;
    }

    public void changeRowType(String key, WikidataClientObjectType newType){
        if(fields.get(key) != null && fields.get(key).getValue() != null) {
            fields.get(key).setType(newType);
            fields.get(key).setLinkInfo(fields.get(key).getValue().toString());
        }
    }

    public void setUpFromEndpointResponse(HashMap response){
        HashMap result = (HashMap) response.get("result");
        List<String> headers = (ArrayList<String>) result.get("variables");
        HashMap<String, Object> row =  ((ArrayList<HashMap>) result.get("rows")).get(0);
        for(String header : headers){
            fields.put(header, new WikidataObject(row.get(header), WikidataClientObjectType.TEXT));
        }
    }
}
