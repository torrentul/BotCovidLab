package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;

import static lv.team3.botcovidlab.processors.ProcessorUtils.*;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CovidStatsProcessor {

    /**@param country Requested country
     * @param date Test date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics */
    public static CovidStats getStats(String country, String date) {
        // TODO Geting object from the list
        return getStats(country, date, date, "00:00:00", "23:59:59").get(0);
    }

    /**@param country Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate Ending date with formatting "YYYY-MM-DD"
     * @return List of CovidStats objects, that are in range [including] fromDate and (excluding) toDate
     * @author Janis Valentinovics */
    public static List<CovidStats> getStats(String country, String fromDate, String toDate) {
        return getStats(country, fromDate, toDate, "00:00:00", "23:59:59");
    }

    /**@param country Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate Starting time with formatting "YYYY-MM-DD"
     * @param fromTime Starting date with formatting "HH:MM:SS"
     * @param toTime Starting time with formatting "HH:MM:SS"
     * @return List of CovidStats objects, that are in range [including] fromDate and [including] toDate
     * @author Janis Valentinovics */
    public static List<CovidStats> getStats(String country, String fromDate, String toDate, String fromTime, String toTime) {
        List<CovidStats> list = new ArrayList<>();
        String requestTemplate = "https://api.covid19api.com/live/country/%s/status/confirmed/date/%s";
        String from = String.format("%sT%sZ", fromDate, fromTime);
        String to = String.format("%sT%sZ", toDate, toTime);
        if(isValidDateString(from) && isValidDateString(to)) {
            String request = String.format(requestTemplate, country, from);
            // TODO Remove this printout
            System.out.println(request);
            try {
                URL url = new URL(request);
                String jsonString = stringFromURL(url);
                JsonArray array = jsonArrayFromString(jsonString);
                array.forEach(object -> {
                    JsonObject jsonObject = object.asJsonObject();
                    String date = jsonObject.getString("Date");
                    /* isValidDateString(date) && isDateInRange(date, from, to, true) */
                    if(true) {
                        list.add(covidStatsOfJsonObject(jsonObject));
                    }
                });
            } catch (Exception e) { /* TODO Proper returns */ }
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

    public static void main(String[] arg) {
        CovidStats stats = getStats("latvia", "2021-01-16");
        System.out.println("Date: " + stats.getDate());
        System.out.println("Died: " + stats.getDeathsTotal() + " Active: " + stats.getActiveTotal() + " Recovered: " + stats.getRecoveredTotal());
    }
}
