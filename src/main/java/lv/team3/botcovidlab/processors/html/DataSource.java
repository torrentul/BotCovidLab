package lv.team3.botcovidlab.processors.html;

public enum DataSource {
    CORONA_WORLDWIDE("https://corona.lmao.ninja/v2/historical/all?lastdays=%d"),
    CORONA_SPECIFIC("https://corona.lmao.ninja/v2/historical/%s?lastdays=%d");
    private final String requestTemplate;

    DataSource(String template) {
        this.requestTemplate = template;
    }

    public String getTemplate() {
        return this.requestTemplate;
    }
}
