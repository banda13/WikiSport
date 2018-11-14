package com.wikidata.sport.Model;

public class WikidataRowObject {

    private Object value;

    private WikidataClientObjectType type;

    private String linkInfo;

    public WikidataRowObject(Object value, WikidataClientObjectType type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public WikidataClientObjectType getType() {
        return type;
    }

    public void setType(WikidataClientObjectType type) {
        this.type = type;
    }

    public String getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo;
    }
}
