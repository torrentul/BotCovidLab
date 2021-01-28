package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.html.DataSource;
import lv.team3.botcovidlab.processors.html.HTMLRequestUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;


public class CovidStatsProcessor {

    @Deprecated
    /**
     * Use automatically determined ranges like getStatsForLastDay
     * To be soon removed
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, String fromDate, String toDate) {
        return getStats(DataSource.CORONA_LMAO_NINJA_API, country, fromDate, toDate);
    }
    @Deprecated
    /**
     * Use automatically determined ranges like getStatsForLastDay
     * To be soon removed
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(DataSource source, String country, String from, String to) {
        return getStats(source, country, new DateStructure(String.format("%sT00:00:00Z", from)), new DateStructure(String.format("%sT23:59:59Z", to)));
    }

    public static List<CovidStats> getStatsForLastDay(String country) {
        DateStructure date = new DateStructure(new Date());
        date.setDay(date.getDay() - 1);
        return getStats(country, date);
    }

    public static List<CovidStats> getStatsForLast7Days(String country) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure(to.toDate());
        from.setDay(from.getDay() - 6);
        return getStats(country, from, to);
    }

    public static List<CovidStats> getStatsForLast30Days(String country) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure(to.toDate());
        from.setDay(from.getDay() - 29);
        return getStats(country, from, to);
    }

    /**
     * @param country Requested country
     * @param date    Test date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, DateStructure date) {
        // TODO Getting object from the list
        return getStats(country, date, date);
    }

    /**
     * @param country  Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate   Starting time with formatting "YYYY-MM-DD"
     * @return List of CovidStats objects, that are in range [including] fromDate and [including] toDate
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, DateStructure fromDate, DateStructure toDate) {
        return getStats(DataSource.CORONA_LMAO_NINJA_API, country, fromDate, toDate);
    }

    // TODO Documentation
    public static List<CovidStats> getStats(DataSource source, String country, DateStructure from, DateStructure to) {
        List<CovidStats> list = new ArrayList<>();
        from.setHour(0);
        from.setMinute(0);
        from.setSecond(0);
        to.setHour(23);
        to.setMinute(59);
        to.setSecond(59);

        JsonArray array = HTMLRequestUtils.jsonFromSource(source, country, from, to);
        array.forEach(object -> {
            CovidStats stats = covidStatsFromJsonObject(object.asJsonObject());
            list.add(stats);
        });
        return list;
    }

    // TODO Documentation
    private static CovidStats covidStatsFromJsonObject(JsonObject object) {
        CovidStats stats = new CovidStats();
        stats.setDate(new DateStructure(object.getString("date")).toDate());
        stats.setCountry(object.getString("country"));
        stats.setDeathsTotal(object.getInt("totalDeaths"));
        stats.setInfectedTotal(object.getInt("totalCases"));
        stats.setRecoveredTotal(object.getInt("totalRecoveries"));
        stats.setActive(object.getInt("activeCases"));
        stats.setDeaths(object.getInt("deaths"));
        stats.setInfected(object.getInt("cases"));
        stats.setRecovered(object.getInt("recoveries"));
        stats.setMissingData(object.getBoolean("missing"));
        return stats;
    }

    // TODO Method which adds missing entries for time periods

    // TODO Remove this when testing processes are done
    public static void main(String[] arg) {
        List<CovidStats> stats = getStatsForLastDay("Latvia");
        System.out.println(stats.size());
        stats.forEach(entry -> {
            System.out.println("Date: " + entry.getDate());
            System.out.println("\tTotal");
            System.out.println("\t\tCases: " + entry.getInfectedTotal() + " Active: " + entry.getActive() + " Recovered: " + entry.getRecoveredTotal() + " Died: " + entry.getDeathsTotal());
            System.out.println("\tThat Day");
            System.out.println("\t\tCases: " + entry.getInfected() + " Recovered: " + entry.getRecovered() + " Died: " + entry.getDeaths());
            System.out.println();
        });
    }
}
