package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.html.DataSource;
import lv.team3.botcovidlab.processors.html.HTMLRequestUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

import static lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;
import static lv.team3.botcovidlab.processors.ProcessorUtils.isValidDateString;


public class CovidStatsProcessor {

    /**
     * @param country Requested country
     * @param date    Test date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, String date) {
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
    public static List<CovidStats> getStats(String country, String fromDate, String toDate) {
        return getStats(DataSource.CORONA_LMAO_NINJA_API, country, fromDate, toDate);
    }

    // TODO Documentation
    public static List<CovidStats> getStats(DataSource source, String country, String fromDate, String toDate) {
        List<CovidStats> list = new ArrayList<>();
        String from = String.format("%sT00:00:00Z", fromDate);
        String to = String.format("%sT23:59:59Z", toDate);
        if (isValidDateString(from) && isValidDateString(to)) {
            JsonArray array = HTMLRequestUtils.jsonFromSource(source, country, new DateStructure(from), new DateStructure(to));
            array.forEach(object -> {
                System.out.println(object.toString());
                CovidStats stats = covidStatsOfJsonObject(object.asJsonObject());
                // TODO
                list.add(stats);
            });
        }
        return list;
    }

    // TODO Documentation
    private static CovidStats covidStatsOfJsonObject(JsonObject object) {
        CovidStats stats = new CovidStats();
        stats.setDate(new DateStructure(object.getString("date")).toDate());
        stats.setCountry(object.getString("country"));
        stats.setDeathsTotal(object.getInt("totalDeaths"));
        stats.setInfectedTotal(object.getInt("totalCases"));
        stats.setRecoveredTotal(object.getInt("totalRecoveries"));
        stats.setDeathsYesterday(object.getInt("yesterdayDeaths"));
        stats.setInfectedYesterday(object.getInt("yesterdayCases"));
        stats.setRecoveredYesterday(object.getInt("yesterdayRecoveries"));
        return stats;
    }

    // TODO Method which adds missing entries for time periods

    // TODO Remove this when testing processes are done
    public static void main(String[] arg) {
        List<CovidStats> stats = getStats(DataSource.CORONA_LMAO_NINJA_API, "latvia", "2021-01-20", "2021-01-27");
        stats.forEach(entry -> {
            System.out.println("Date: " + entry.getDate());
            System.out.println("Died: " + entry.getDeathsTotal() + " Active: " + entry.getActiveTotal() + " Recovered: " + entry.getRecoveredTotal());
        });
    }
}
