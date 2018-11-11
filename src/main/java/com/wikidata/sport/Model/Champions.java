package com.wikidata.sport.Model;

//TEST
public class Champions {

    private String season;

    private String first;

    private String second;

    private String third;

    public Champions() {
    }

    public Champions(String season, String first, String second, String third) {
        this.season = season;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }
}
