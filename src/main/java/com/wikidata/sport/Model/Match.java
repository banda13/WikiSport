package com.wikidata.sport.Model;

import com.wikidata.sport.Services.WikidataConsts;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Match {

    private String team1;

    private String team2;

    private String team1Id;

    private String team2Id;

    private String winner;

    private String winnerId;

    private int team1Goals;

    private int team2Goals;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date matchDate;

    private String wikidataId;

    public Match() {
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(String team1Id) {
        this.team1Id = team1Id.substring(team1Id.lastIndexOf("/") + 1);
    }

    public String getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(String team2Id) {
        this.team2Id = team2Id.substring(team2Id.lastIndexOf("/") + 1);
    }

    public int getTeam1Goals() {
        return team1Goals;
    }

    public void setTeam1Goals(int team1Goals) {
        this.team1Goals = team1Goals;
    }

    public int getTeam2Goals() {
        return team2Goals;
    }

    public void setTeam2Goals(int team2Goals) {
        this.team2Goals = team2Goals;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public String getWikidataId() {
        return wikidataId;
    }

    public void setWikidataId(String wikidataId) {
        this.wikidataId = wikidataId;
    }

    public String getWinner() {
        if(team1Goals >team2Goals){
            return team1;
        }
        else if(team2Goals > team1Goals){
            return team2;
        }
        else{
            return "Draw";
        }
    }

    public String getWinnerId() {
        if(team1Goals >team2Goals){
            return team1Id;
        }
        else if(team2Goals > team1Goals){
            return team2Id;
        }
        else{
            return WikidataConsts.DRAW;
        }
    }
}
