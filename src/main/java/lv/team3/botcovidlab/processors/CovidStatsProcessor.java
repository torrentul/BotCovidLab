package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.html.DataSource;
import lv.team3.botcovidlab.processors.html.HTMLRequestUtils;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;
import static lv.team3.botcovidlab.processors.ProcessorUtils.isValidDateString;


public class CovidStatsProcessor {

    /**
     * @param country Requested country
     * @param date    Test date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics
     */
    public static CovidStats getStats(String country, String date) {
        // TODO Getting object from the list
        return getStats(country, date, date, "00:00:00", "23:59:59").get(0);
    }

    /**
     * @param country  Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate   Ending date with formatting "YYYY-MM-DD"
     * @return List of CovidStats objects, that are in range [including] fromDate and (excluding) toDate
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, String fromDate, String toDate) {
        return getStats(country, fromDate, toDate, "00:00:00", "23:59:59");
    }

    /**
     * @param country  Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate   Starting time with formatting "YYYY-MM-DD"
     * @param fromTime Starting date with formatting "HH:MM:SS"
     * @param toTime   Starting time with formatting "HH:MM:SS"
     * @return List of CovidStats objects, that are in range [including] fromDate and [including] toDate
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStats(String country, String fromDate, String toDate, String fromTime, String toTime) {
        List<CovidStats> list = new ArrayList<>();
        String from = String.format("%sT%sZ", fromDate, fromTime);
        String to = String.format("%sT%sZ", toDate, toTime);
        if (isValidDateString(from) && isValidDateString(to)) {
            JsonArray array = HTMLRequestUtils.jsonFromSource(DataSource.COVID_19_API, country, new DateStructure(from), new DateStructure(to));
            array.forEach(object -> {
                JsonObject jsonObject = object.asJsonObject();
                String date = jsonObject.getString("Date");
                // TODO This is wrong
                list.add(covidStatsOfJsonObject(jsonObject));
            });
        }
        return list;
    }

    private static CovidStats covidStatsOfJsonObject(JsonObject object) {
        CovidStats stats = new CovidStats();
        stats.setDate(new DateStructure(object.getString("Date")).toDate());
        stats.setDeathsTotal(object.getInt("Deaths"));
        stats.setInfectedTotal(object.getInt("Confirmed"));
        stats.setRecoveredTotal(object.getInt("Recovered"));
        stats.setActiveTotal(object.getInt("Active"));
        return stats;
    }
}
