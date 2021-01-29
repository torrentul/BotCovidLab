package lv.team3.botcovidlab.processors.html;

public enum DataSource {
    CORONA_WORLDWIDE("https://corona.lmao.ninja/v2/historical/all?lastdays=%d"),
    CORONA_SPECIFIC("https://corona.lmao.ninja/v2/historical/%s?lastdays=%d"),
    CORONA_SPECIFIC_TODAY("https://corona.lmao.ninja/v2/countries/%s?yesterday=false"),
    CORONA_WORLDWIDE_TODAY("https://corona.lmao.ninja/v2/continents?yesterday=false");
    private final String requestTemplate;

    DataSource(String template) {
        this.requestTemplate = template;
    }

    public String getTemplate() {
        return this.requestTemplate;
    }

    public static DataSource historicFromString(String location) {
        return "world".equalsIgnoreCase(location) ? CORONA_WORLDWIDE : CORONA_SPECIFIC;
    }

    public static DataSource latestFromString(String location) {
        return "world".equalsIgnoreCase(location) ? CORONA_WORLDWIDE_TODAY : CORONA_SPECIFIC_TODAY;
    }
}
