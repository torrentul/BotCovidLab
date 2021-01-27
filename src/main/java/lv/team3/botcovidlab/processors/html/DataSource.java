package lv.team3.botcovidlab.processors.html;

public enum DataSource {
    COVID_19_API("https://api.covid19api.com/live/country/%s/status/confirmed/date/%s"),
    TEST0_API(""),
    TEST1_API("");

    private final String requestTemplate;

    DataSource(String template) {
        this.requestTemplate = template;
    }

    public String getTemplate() {
        return this.requestTemplate;
    }
}
