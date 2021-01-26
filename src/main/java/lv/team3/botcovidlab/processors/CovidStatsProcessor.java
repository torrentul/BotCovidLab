package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;

public class CovidStatsProcessor {
    static String requestTemplate = "https://api.covid19api.com/country/%s/status/confirmed?from=%s&to=%s";
    static String requestTemplate2 = "https://api.covid19api.com/live/country/south-africa/status/confirmed/date/2020-03-21T13:13:30Z";

    /*
     * @param country Requested country
     * @param fromDate Test date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics
     */
    public static CovidStats getStats(String country, String date) {
        return getStats(country, date, date, "00:00:00", "23:59:59");
    }

    /*
     * @param country Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate Ending date with formatting "YYYY-MM-DD"
     * @author Janis Valentinovics
     */
    public static CovidStats getStats(String country, String fromDate, String toDate) {
        return getStats(country, fromDate, toDate, "00:00:00", "00:00:00");
    }

    /*
     * @param country Requested country
     * @param fromDate Starting date with formatting "YYYY-MM-DD"
     * @param toDate Starting time with formatting "YYYY-MM-DD"
     * @param fromTime Starting date with formatting "HH:MM:SS"
     * @param toTime Starting time with formatting "HH:MM:SS"
     * @author Janis Valentinovics
     */
    public static CovidStats getStats(String country, String fromDate, String toDate, String fromTime, String toTime) {
        String from = String.format("%sT%sZ", fromDate, fromTime);
        String to = String.format("%sT%sZ", toDate, toTime);
        String request = String.format(requestTemplate, country, from, to);
        System.out.println(request);
        try {
            URL url = new URL(request);
            String jsonString = stringFromURL(url);
            System.out.println(jsonString);
            JsonArray array = jsonArrayFromString(jsonString);
            // TODO Parse array data into CovidStats object
        } catch (Exception e) {
            e.printStackTrace();
        }
        // FIXME This is just wrong...
        return new CovidStats("Latvija", 100, 200, 1000, new Date());
    }

    /*
     * @param url URL returning json string
     * @author Janis Valentinovics
     */
    private static String stringFromURL(URL url) {
        String ret = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringWriter writer = new StringWriter();
            String line = "";
            while((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            ret = writer.toString();

            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret != null ? ret : "[]";
    }

    /*
     * @param string Json string to be parsed into json array
     * @author Janis Valentinovics
     */
    private static JsonArray jsonArrayFromString(String string) {
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    public static void main(String[] arg) {
        getStats("latvia", "2021-11-01", "2021-20-01", "00:00:00", "00:00:00");
        getStats("latvia", "2020-01-01");
        getStats("latvia", "2021-01-01", "2020-01-01");
    }
}
