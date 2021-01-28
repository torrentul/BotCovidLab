package lv.team3.botcovidlab.adapter.facebook;

import lv.team3.botcovidlab.CovidStats;

import java.util.List;

import static lv.team3.botcovidlab.adapter.facebook.DateUtility.getYesterdayDate;
import static lv.team3.botcovidlab.processors.CovidStatsProcessor.getStats;

public class TotalStatUtil {

    public static String countTotal(String country, String dateFrom, String dateTo){
        List<CovidStats> stats = getStats(country,dateFrom, getYesterdayDate());
        int deathTotal = stats.stream().reduce(0, (deaths, stat) -> deaths + stat.getDeathsTotal(), Integer::sum);
        int activeTotal = stats.stream().reduce(0, (active, stat) -> active + stat.getActiveTotal(), Integer::sum);
        int recoveredTotal = stats.stream().reduce(0, (recovered, stat) -> recovered + stat.getRecoveredTotal(), Integer::sum);

        return ("\uD83C\uDDF1\uD83C\uDDFB " + "From: " + dateFrom + " Till: " + dateTo + '\n' +
                " Died: " + deathTotal + '\n' + " Active: " + activeTotal + '\n' + " Recovered: " + recoveredTotal);
    }
}
