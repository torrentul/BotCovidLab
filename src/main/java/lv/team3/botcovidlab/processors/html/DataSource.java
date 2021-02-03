package lv.team3.botcovidlab.processors.html;

/**
 * Enum containing websites for Covid-19 statistics data.<br>
 * {@link #CORONA_WORLDWIDE}<br>
 * {@link #CORONA_WORLDWIDE_TODAY}<br>
 * {@link #CORONA_SPECIFIC}<br>
 * {@link #CORONA_SPECIFIC_TODAY}<br>
 * @author Janis Valentinovics
 */
public enum DataSource {

    /**
     * Website for world historical statistics data request.<br>
     * Has 1 field <code><b>int</b>days</code> this field represents days in history for how many days statistics
     * should be returned, ending with yesterday.
     */
    CORONA_WORLDWIDE("https://corona.lmao.ninja/v2/historical/all?lastdays=%d"),

    /**
     * Website for country historical statistics data request.<br>
     * Has 2 fields <code><b>String</b> country</code> and <code><b>int</b> days</code>. Field
     * <code><b>String</b>country</code> is country for which data should be returned. Field
     * <code><b>int</b>days</code> represents days in history for how many days statistics should be returned,
     * ending with yesterday.
     */
    CORONA_SPECIFIC("https://corona.lmao.ninja/v2/historical/%s?lastdays=%d"),

    /**
     * Website for country current statistics data request.<br>
     * Has 1 field <code><b>String</b> country</code> this field is country for which data should be returned.
     */
    CORONA_SPECIFIC_TODAY("https://corona.lmao.ninja/v2/countries/%s?yesterday=false"),

    /**
     * Website for world current statistics data request.<br>
     */
    CORONA_WORLDWIDE_TODAY("https://corona.lmao.ninja/v2/continents?yesterday=false");

    private final String requestTemplate;

    DataSource(String template) {
        this.requestTemplate = template;
    }

    /**
     * Returns specified template for the enum.
     * @return Template for HTML request.<br>
     * {@link #CORONA_WORLDWIDE}<br>
     * {@link #CORONA_WORLDWIDE_TODAY}<br>
     * {@link #CORONA_SPECIFIC}<br>
     * {@link #CORONA_SPECIFIC_TODAY}<br>
     * @author Janis Valentinovics
     */
    public String getTemplate() {
        return this.requestTemplate;
    }

    /**
     * Returns specified DataSource for passed location. If location is <code>"world"</code> returns
     * {@link #CORONA_WORLDWIDE}, else {@link #CORONA_SPECIFIC}.
     * @param location <code>"world"</code> or any country lowercase. Example "latvia".
     * @return {@link #CORONA_WORLDWIDE} or {@link #CORONA_SPECIFIC}.
     * @author Janis Valentinovics
     */
    public static DataSource historicFromString(String location) {
        return "world".equalsIgnoreCase(location) ? CORONA_WORLDWIDE : CORONA_SPECIFIC;
    }

    /**
     * Returns specified DataSource for passed location. If location is <code>"world"</code> returns
     * {@link #CORONA_WORLDWIDE_TODAY}, else {@link #CORONA_SPECIFIC_TODAY}.
     * @param location <code>"world"</code> or any country lowercase. Example "latvia".
     * @return {@link #CORONA_WORLDWIDE_TODAY} or {@link #CORONA_SPECIFIC_TODAY}.
     * @author Janis Valentinovics
     */
    public static DataSource latestFromString(String location) {
        return "world".equalsIgnoreCase(location) ? CORONA_WORLDWIDE_TODAY : CORONA_SPECIFIC_TODAY;
    }
}
