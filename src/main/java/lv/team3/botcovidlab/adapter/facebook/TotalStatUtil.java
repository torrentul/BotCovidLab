package lv.team3.botcovidlab.adapter.facebook;

import lv.team3.botcovidlab.CovidStats;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static lv.team3.botcovidlab.adapter.facebook.DateUtility.getYesterdayDate;
import static lv.team3.botcovidlab.processors.CovidStatsProcessor.*;

public class TotalStatUtil {

    public static String countTotalThirtyDays(String country) {
        List<CovidStats> stats = getStatsForLast30Days(country);
        int deathTotal = stats.stream().reduce(0, (deaths, stat) -> deaths + stat.getDeaths(), Integer::sum);
        int recoveredTotal = stats.stream().reduce(0, (recovered, stat) -> recovered + stat.getRecovered(), Integer::sum);
        int casesTotal = stats.stream().reduce(0, (cases, stat) -> cases + stat.getInfected(), Integer::sum);


        return (StringUtils.capitalize(country) + "\n" +
                "For the last thirty days " + "\n" +
                "Cases: " + casesTotal + "\n" +
                "Recovered: " + recoveredTotal + "\n" +
                "Died: " + deathTotal + "\n" + "\n" +
                "Total" + "\n" +
                "Cases: " + stats.get(0).getInfectedTotal() + "\n" +
                "Active: " + stats.get(0).getActive() + "\n" +
                "Recovered: " + stats.get(0).getRecoveredTotal() + "\n" +
                "Died: " + stats.get(0).getDeathsTotal());
    }

    public static String countTotalSevenDays(String country) {
        List<CovidStats> stats = getStatsForLast7Days(country);
        int deathTotal = stats.stream().reduce(0, (deaths, stat) -> deaths + stat.getDeaths(), Integer::sum);
        int activeTotal = stats.stream().reduce(0, (active, stat) -> active + stat.getActive(), Integer::sum);
        int recoveredTotal = stats.stream().reduce(0, (recovered, stat) -> recovered + stat.getRecovered(), Integer::sum);
        int casesTotal = stats.stream().reduce(0, (cases, stat) -> cases + stat.getInfected(), Integer::sum);


        return (StringUtils.capitalize(country) + "\n" +
                "For the last seven days " + "\n" +
                "Cases: " + casesTotal + "\n" +
                "Recovered: " + recoveredTotal + "\n" +
                "Died: " + deathTotal + "\n" + "\n" +
                "Total" + "\n" +
                "Cases: " + stats.get(0).getInfectedTotal() + "\n" +
                "Active: " + stats.get(0).getActive() + "\n" +
                "Recovered: " + stats.get(0).getRecoveredTotal() + "\n" +
                "Died: " + stats.get(0).getDeathsTotal());
    }

    public static String countTotalYesterday(String country) {
        List<CovidStats> stats = getStatsForLastDay(country);
        return (StringUtils.capitalize(country) + "\n" +
                "Date: " + getYesterdayDate() + "\n" +
                "Cases: " + stats.get(0).getInfected() + "\n" +
                "Recovered: " + stats.get(0).getRecovered() + "\n" +
                "Died: " + stats.get(0).getDeaths() + "\n" + "\n" +
                "Total: " + "\n" +
                "Cases: " + stats.get(0).getInfectedTotal() + "\n" +
                "Active: " + stats.get(0).getActive() + "\n" +
                "Recovered: " + stats.get(0).getRecoveredTotal() + "\n" +
                "Died: " + stats.get(0).getDeathsTotal());
    }


}
