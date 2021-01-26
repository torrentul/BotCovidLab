package lv.team3.botcovidlab;

import java.util.Date;

public class CovidStatsProcessor {
    public static CovidStats getStats(String country, String date){
        return lv.team3.botcovidlab.processors.CovidStatsProcessor.getStats(country, date);
    }
}
