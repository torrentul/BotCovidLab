package lv.team3.botcovidlab;

public class CovidStatsProcessor {
    @Deprecated
    /*
     * Moved to lv.team3.botcovidlab.processors.CovidStatsProcessor.getStats(...)
     */
    public static CovidStats getStats(String country, String date){
        return lv.team3.botcovidlab.processors.CovidStatsProcessor.getStats(country, date);
    }
}
