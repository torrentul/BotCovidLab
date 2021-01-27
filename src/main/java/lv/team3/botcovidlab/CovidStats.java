package lv.team3.botcovidlab;

import java.util.Date;
import java.lang.System.Logger.Level;

public class CovidStats {

    private String country;
    private int infectedToday;
    private int deathsToday;
    private int recoveredToday;
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
     * Use getInfectedTotal / getInfectedToday
     */
    public void setInfected(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setInfectedTotal(t);
    }
    @Deprecated
    /*
     * Use setInfectedTotal / setInfectedToday
     */
    public int getInfected() {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        return this.getInfectedTotal();
    }

    @Deprecated
    /*
     * Use getDeathsTotal / getDeathsToday
     */
    public void setDeaths(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setDeathsTotal(t);
    }
    @Deprecated
    /*
     * Use setDeathsTotal / setDeathsToday
     */
    public int getDeaths() {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        return this.getDeathsTotal();
    }

    @Deprecated
    /*
     * Use getRecoveredTotal / getRecoveredToday
     */
    public void setRecovered(int t) {
        System.getLogger("CovidStats").log(Level.WARNING, "Using deprecated method");
        this.setRecoveredTotal(t);
    }
    @Deprecated
    /*
     * Use setRecoveredTotal / setRecoveredToday
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

    public int getInfectedToday() {
        return infectedToday;
    }

    public void setInfectedToday(int infectedToday) {
        this.infectedToday = infectedToday;
    }

    public int getDeathsToday() {
        return deathsToday;
    }

    public void setDeathsToday(int deathsToday) {
        this.deathsToday = deathsToday;
    }

    public int getRecoveredToday() {
        return recoveredToday;
    }

    public void setRecoveredToday(int recoveredToday) {
        this.recoveredToday = recoveredToday;
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
