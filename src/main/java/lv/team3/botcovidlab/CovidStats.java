package lv.team3.botcovidlab;

import java.util.Date;
import java.lang.System.Logger.Level;

public class CovidStats {

    private String country;
    private int infectedYesterday;
    private int deathsYesterday;
    private int recoveredYesterday;
    private int activeYesterday;
    private int infectedTotal;
    private int deathsTotal;
    private int recoveredTotal;
    private int activeTotal;
    private Date date;

    public int getActiveTotal() {
        return activeTotal;
    }

    public void setActiveTotal(int activeTotal) {
        this.activeTotal = activeTotal;
    }

    @Deprecated
    /*
     * Use getInfectedTotal / getinfectedYesterday
     */
    public void setInfected(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setInfectedTotal(t);
    }
    @Deprecated
    /*
     * Use setInfectedTotal / setinfectedYesterday
     */
    public int getInfected() {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        return this.getInfectedTotal();
    }

    @Deprecated
    /*
     * Use getDeathsTotal / getdeathsYesterday
     */
    public void setDeaths(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setDeathsTotal(t);
    }
    @Deprecated
    /*
     * Use setDeathsTotal / setdeathsYesterday
     */
    public int getDeaths() {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        return this.getDeathsTotal();
    }

    @Deprecated
    /*
     * Use getRecoveredTotal / getrecoveredYesterday
     */
    public void setRecovered(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setRecoveredTotal(t);
    }
    @Deprecated
    /*
     * Use setRecoveredTotal / setrecoveredYesterday
     */
    public int getRecovered() {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        return this.getRecoveredTotal();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getInfectedYesterday() {
        return infectedYesterday;
    }

    public void setInfectedYesterday(int infectedYesterday) {
        this.infectedYesterday = infectedYesterday;
    }

    public int getDeathsYesterday() {
        return deathsYesterday;
    }

    public void setDeathsYesterday(int deathsYesterday) {
        this.deathsYesterday = deathsYesterday;
    }

    public int getRecoveredYesterday() {
        return recoveredYesterday;
    }

    public void setRecoveredYesterday(int recoveredYesterday) {
        this.recoveredYesterday = recoveredYesterday;
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
