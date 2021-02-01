package lv.team3.botcovidlab;

import java.util.Date;

public class CovidStats {

    private String country;
    private int infected;
    private int deaths;
    private int recovered;
    private int infectedTotal;
    private int deathsTotal;
    private int recoveredTotal;
    private int activeTotal;
    private Date date;
    private boolean missingData;

    public boolean isMissingData() {
        return missingData;
    }

    public void setMissingData(boolean missingData) {
        this.missingData = missingData;
    }

    public int getActive() {
        return activeTotal;
    }

    public void setActive(int activeTotal) {
        this.activeTotal = activeTotal;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getInfected() {
        return infected;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }

    public int getInfectedTotal() {
        return infectedTotal;
    }

    public void setInfectedTotal(int infectedTotal) {
        this.infectedTotal = infectedTotal;
    }

    public int getDeathsTotal() {
        return deathsTotal;
    }

    public void setDeathsTotal(int deathsTotal) {
        this.deathsTotal = deathsTotal;
    }

    public int getRecoveredTotal() {
        return recoveredTotal;
    }

    public void setRecoveredTotal(int recoveredTotal) {
        this.recoveredTotal = recoveredTotal;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
