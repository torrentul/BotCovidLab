package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;
import lv.team3.botcovidlab.processors.html.HTMLRequestUtils;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lv.team3.botcovidlab.utils.DateUtils.DateStructure;

public class CovidStatsProcessor {

    private CovidStatsProcessor() {}

    /**
     * Method used to get Covid-19 statistics data object.
     * Returns latest statistics data.
     *
     * @param location String <code>"world"</code> or any country
     * @return List of CovidStats containing latest data
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStatsForLatest(String location) {
        return getLatestStats(location);
    }

    /**
     * Method used to get Covid-19 statistics data object.
     * Returns yesterdays statistics data.
     *
     * @param location String <code>"world"</code> or any country
     * @return List of CovidStats containing data for yesterday
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStatsForLastDay(String location) {
        DateStructure date = new DateStructure(new Date());
        date.setDay(date.getDay() - 1);
        return getStats(location, date, date);
    }

    /**
     * Method used to get Covid-19 statistics data object.
     * Returns list of last 7 days statistics data (excluding) today.
     *
     * @param location String <code>"world"</code> or any country
     * @return List of CovidStats containing data for last 7 days
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStatsForLast7Days(String location) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure(to.toDate());
        from.setDay(from.getDay() - 6);
        return getStats(location, from, to);
    }

    /**
     * Method used to get Covid-19 statistics data object.
     * Returns list of last 30 days statistics data (excluding) today.
     *
     * @param location String <code>"world"</code> or any country
     * @return List of CovidStats containing data for last 30 days
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStatsForLast30Days(String location) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure(to.toDate());
        from.setDay(from.getDay() - 29);
        return getStats(location, from, to);
    }

    /**
     * Method used to get Covid-19 statistics data object.
     * Returns statistics data starting from 22.01.2020 till [including] yesterday.
     *
     * @param location String <code>"world"</code> or any country
     * @return List of CovidStats containing data for all covid history
     * @author Janis Valentinovics
     */
    public static List<CovidStats> getStatsFromBeginning(String location) {
        DateStructure to = new DateStructure(new Date());
        to.setDay(to.getDay() - 1);
        DateStructure from = new DateStructure("2020-01-22T00:00:00Z");
        return getStats(location, from, to);
    }

    /**
     * Method used to get historical Covid-19 statistics data object.
     *
     * @param location Requested country
     * @param from     Starting from date [including]
     * @param to       Ending with [including]
     * @return List of CovidStats objects, that are in range [including] fromDate and [including] toDate
     * @author Janis Valentinovics
     */
    private static List<CovidStats> getStats(String location, DateStructure from, DateStructure to) {
        from.setHour(0); from.setMinute(0); from.setSecond(0); //@auto:off
        to.setHour(23); to.setMinute(59); to.setSecond(59);
        //@auto:on
        JsonObject object = HTMLRequestUtils.getHistoricData(location, from, to);
        List<CovidStats> list = new ArrayList<>();
        object.keySet().forEach(key -> {
            CovidStats stats = covidStatsFromJsonObject(object.getJsonObject(key));
            stats.setDate(new DateStructure(key).toDate());
            list.add(stats);
        });
        return list;
    }

    /**
     * Method used to get latest Covid-19 statistics data object.
     *
     * @param location Requested country
     * @return List of latest CovidStats objects
     * @author Janis Valentinovics
     */
    private static List<CovidStats> getLatestStats(String location) {
        JsonObject object = HTMLRequestUtils.getLatestData(location);
        List<CovidStats> list = new ArrayList<>();
        object.keySet().forEach(key -> {
            CovidStats stats = covidStatsFromJsonObject(object.getJsonObject(key));
            stats.setDate(new DateStructure(key).toDate());
            list.add(stats);
        });
        return list;
    }

    /**
     * Parses JsonObject into CovidStats object
     *
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
    }}

