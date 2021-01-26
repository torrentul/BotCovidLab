package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.CovidStats;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class CovidStatsProcessor {
    static String requestTemplate = "https://api.covid19api.com/country/south-africa/status/confirmed?from=2020-03-01T00:00:00Z&to=2020-04-01T00:00:00Z";
    static String timeTemplate = "%04d-%02d-%02dT%02d:%02d:%02dZ";

    public static CovidStats getStats(String country, String date) {
        String request = "";
        try {
            URL url = new URL(request);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return new CovidStats("Latvia",100,200,1000,new Date());
    }

    public static void main(String[] arg) {
        System.out.println(String.format(timeTemplate, 12, 23, 123, 123, 34, 56));
        System.out.println(String.format(timeTemplate, 12, 23, 123, 123, 34, 56));
        System.out.println(String.format(timeTemplate, 12, 23, 123, 123, 34, 56));
    }
}
