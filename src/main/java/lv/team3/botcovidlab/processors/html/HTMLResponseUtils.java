package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class HTMLResponseUtils {

    public static JsonObject jsonObjectFromCoronaLmaoNinjaApi(JsonObject object, String key, String previousKey, DateStructure date) {
        JsonObject cases = object.getJsonObject("timeline").getJsonObject("cases");
        JsonObject recoveries = object.getJsonObject("timeline").getJsonObject("recovered");
        JsonObject deaths = object.getJsonObject("timeline").getJsonObject("deaths");
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("country", object.getString("country"));
        builder.add("date", date.toString());
        builder.add("totalCases", cases.getInt(key));
        builder.add("totalDeaths", deaths.getInt(key));
        builder.add("totalRecoveries", recoveries.getInt(key));
        builder.add("activeCases",cases.getInt(key) - deaths.getInt(key) - recoveries.getInt(key));
        int pc = 0, pd = 0, pr = 0;
        try {
            pc = cases.getInt(key) - cases.getInt(previousKey);
            pd = deaths.getInt(key) - deaths.getInt(previousKey);
            pr = recoveries.getInt(key) - recoveries.getInt(previousKey);
            builder.add("missing", false);
        } catch (Exception e) {
            builder.add("missing", true);
        }
        builder.add("cases", pc);
        builder.add("deaths",  pd);
        builder.add("recoveries", pr);
        return builder.build();
    }
}
