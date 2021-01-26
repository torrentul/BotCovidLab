package lv.team3.botcovidlab;

import java.util.Date;

public class CovidStats {
    private String country;
    private int infected;
    private int deaths;
    private int recovered;
    private Date date;

    public CovidStats(String country, int infected, int deaths, int recovered, Date date) {
        this.country = country;
        this.infected = infected;
        this.deaths = deaths;
        this.recovered = recovered;
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public int getInfected() {
        return infected;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public Date getDate() {
        return date;
    }
}
