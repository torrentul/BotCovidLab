package lv.team3.botcovidlab.processors.html;

public enum DataSource {
    CORONA_LMAO_NINJA_API("https://corona.lmao.ninja/v2/historical/%s?lastdays=%d");

    private final String requestTemplate;

    DataSource(String template) {
        this.requestTemplate = template;
    }

    public String getTemplate() {
        return this.requestTemplate;
    }
}
