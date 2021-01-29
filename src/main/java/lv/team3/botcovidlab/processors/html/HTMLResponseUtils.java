package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class HTMLResponseUtils {

    public static JsonObject jsonObjectForSpecific(JsonObject object, String key, String previousKey) {
        JsonObject cases = object.getJsonObject("timeline").getJsonObject("cases");
        JsonObject recoveries = object.getJsonObject("timeline").getJsonObject("recovered");
        JsonObject deaths = object.getJsonObject("timeline").getJsonObject("deaths");
        return jsonObject(object, cases, deaths, recoveries, key, previousKey);
    }

    public static JsonObject jsonObjectForWorldwide(JsonObject object, String key, String previousKey) {
        JsonObject cases = object.getJsonObject("cases");
        JsonObject recoveries = object.getJsonObject("recovered");
        JsonObject deaths = object.getJsonObject("deaths");
        return jsonObject(object, cases, deaths, recoveries, key, previousKey);
    }

    private static JsonObject jsonObject(JsonObject root,JsonObject cas, JsonObject rec, JsonObject dea, String key, String previousKey) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("country", "world");
        builder.add("totalCases", cas.getInt(key));
        builder.add("totalDeaths", dea.getInt(key));
        builder.add("totalRecoveries", rec.getInt(key));
        builder.add("activeCases",cas.getInt(key) - dea.getInt(key) - rec.getInt(key));
        int pc = 0, pd = 0, pr = 0;
        try {
            pc = cas.getInt(key) - cas.getInt(previousKey);
            pd = dea.getInt(key) - dea.getInt(previousKey);
            pr = rec.getInt(key) - rec.getInt(previousKey);
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
