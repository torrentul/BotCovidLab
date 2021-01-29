package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.html.HTMLRequestUtils;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;


public class CovidStatsProcessor {

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

    public static List<CovidStats> getStatsFromBeginning(String country) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure("2020-01-22T00:00:00Z");
        return getStats(country, from, to);
    }

    /**
     * @param country Requested country
     * @param date    Requested date
     * @author Janis Valentinovics
     */
    private static List<CovidStats> getStats(String country, DateStructure date) {
        // TODO Getting object from the list
        return getStats(country, date, date);
    }

    /**
     * @param country Requested country
     * @param from    Starting from date [including]
     * @param to      Ending with [including]
     * @return List of CovidStats objects, that are in range [including] fromDate and [including] toDate
     * @author Janis Valentinovics
     */
    private static List<CovidStats> getStats(String country, DateStructure from, DateStructure to) {
        from.setHour(0); from.setMinute(0); from.setSecond(0); // @formatter:off
        to.setHour(23); to.setMinute(59); to.setSecond(59);    // @formatter:off
        JsonObject object = HTMLRequestUtils.jsonFromSource(country, from, to);
        List<CovidStats> list = new ArrayList<>();
        object.keySet().forEach(key -> {
            CovidStats stats = covidStatsFromJsonObject(object.getJsonObject(key));
            stats.setDate(new DateStructure(key).toDate());
            list.add(stats);
        });
        return list;
    }

    /**
     * @param object JsonObject with keys <code>country totalDeaths totalCases totalRecoveries activeCases
     *               deaths cases recoveries missing</code>
     * @return CovidStats object with all fields from JsonObject <code>object</code> except date
     * @author Janis Valentinovics
     */
    private static CovidStats covidStatsFromJsonObject(JsonObject object) {
        CovidStats stats = new CovidStats();
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

    // TODO Remove this when testing processes are done
    public static void main(String[] arg) {
        List<CovidStats> stats = getStatsForLast30Days("world");
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
