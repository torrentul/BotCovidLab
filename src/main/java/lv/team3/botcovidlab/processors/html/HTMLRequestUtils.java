package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils;
import lv.team3.botcovidlab.utils.DateUtils.DateStructure;

import javax.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HTMLRequestUtils {

    // TODO Documentation
    private static String stringFromURL(URL url) {
        String ret = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringWriter writer = new StringWriter();
            String line = "";
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            ret = writer.toString();

            writer.flush();
            writer.close();
        } catch (Exception e) { /* TODO Proper returns */ }
        return ret;
    }

    // TODO Documentation
    private static JsonObject jsonObjectFromURL(URL url) {
        String ret, string = (ret = stringFromURL(url)) != null ? ret : "{}";
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonObject array = reader.readObject();
        reader.close();
        return array;
    }

    // TODO Documentation
    private static JsonArray jsonArrayFromURL(URL url) {
        String ret, string = (ret = stringFromURL(url)) != null ? ret : "[]";
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    // TODO Documentation
    public static JsonObject getHistoricData(String location, DateStructure from, DateStructure to) {
        DataSource structure = DataSource.historicFromString(location);
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            DateStructure currentDate = new DateStructure(new Date());
            long mils = Math.abs(from.toDate().getTime() - currentDate.toDate().getTime());
            long days = TimeUnit.DAYS.convert(mils, TimeUnit.MILLISECONDS) + 2;
            String request;
            JsonObject jsonObject;
            if ("world".equals(location)) {
                request = String.format(structure.getTemplate(), days);
                jsonObject = jsonObjectFromURL(new URL(request));
            } else {
                request = String.format(structure.getTemplate(), location, days);
                jsonObject = jsonObjectFromURL(new URL(request)).getJsonObject("timeline");
            }
            for (String key : jsonObject.getJsonObject("cases").keySet()) {
                int[] parts = new int[3];
                for (int i = 0; i < 3; i++) {
                    parts[i] = Integer.parseInt(key.split("/")[i]);
                }
                String dateString = String.format("20%02d-%02d-%02dT00:00:00Z", parts[2], parts[0], parts[1]);
                if (ProcessorUtils.isValidDateString(dateString)) {
                    DateStructure struct = new DateStructure(dateString);
                    DateStructure struct2 = new DateStructure(struct.toDate());
                    struct2.setDay(struct2.getDay() - 1);
                    String pk = String.format("%d/%d/%d", struct2.getMonth(), struct2.getDay(), struct2.getYear() % 100);
                    if (ProcessorUtils.isDateInRange(struct, from, to, true)) {
                        objectBuilder.add(
                                struct.toString(),
                                parseHistoricData(location, jsonObject, key, pk)
                        );
                    }
                }
            }
        } catch (Exception e) { /* TODO Exception */}
        return objectBuilder.build();
    }

    private static JsonObject parseHistoricData(String place, JsonObject obj, String key, String previousKey) {
        JsonObject cas = obj.getJsonObject("cases");
        JsonObject rec = obj.getJsonObject("recovered");
        JsonObject dea = obj.getJsonObject("deaths");
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add("country", place);
        builder.add("totalCases", cas.getInt(key));
        builder.add("totalDeaths", dea.getInt(key));
        builder.add("totalRecoveries", rec.getInt(key));
        builder.add("activeCases", cas.getInt(key) - dea.getInt(key) - rec.getInt(key));
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
        builder.add("deaths", pd);
        builder.add("recoveries", pr);
        return builder.build();
    }

    public static JsonObject getLatestData(String location) {
        DataSource source = DataSource.latestFromString(location);
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        DateStructure struct = new DateStructure(new Date());
        if (source == DataSource.CORONA_SPECIFIC_TODAY) {
            try {
                String request = String.format(source.getTemplate(), location);
                JsonObject nativeObject = jsonObjectFromURL(new URL(request));
                objectBuilder.add(struct.toString(), parseLocationLatestData(location, nativeObject));
            } catch (MalformedURLException ignored) {
            }
        } else {
            try {
                String request = source.getTemplate();
                JsonArray nativeArray = jsonArrayFromURL(new URL(request));
                objectBuilder.add(struct.toString(), parseContinentLatestData(nativeArray));
            } catch (MalformedURLException ignored) {
            }
        }
        return objectBuilder.build();
    }

    private static JsonObject parseContinentLatestData(JsonArray arr) {
        final String[] keys = {"totalCases", "totalDeaths", "totalRecoveries", "activeCases", "cases", "deaths", "recoveries"};
        JsonObjectBuilder builder = Json.createObjectBuilder();
        try {
            JsonObject ret = parseLocationLatestData("world", arr.getJsonObject(0));
            for (int i = 1; i < arr.size(); i++) {
                JsonObject current = parseLocationLatestData("world", arr.getJsonObject(i));
                for (String key : keys) {
                    builder.add(key, ret.getInt(key) + current.getInt(key));
                }
                if(i != arr.size() - 1) {
                    ret = builder.build();
                }
            }
            builder.add("missing", false);
        } catch (Exception ignored) {
            builder.add("missing", true);
        }
        builder.add("country", "world");
        return builder.build();

    }

    private static JsonObject parseLocationLatestData(String place, JsonObject obj) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        try {
            builder.add("country", place);
            builder.add("totalCases", obj.getInt("cases"));
            builder.add("totalDeaths", obj.getInt("deaths"));
            builder.add("totalRecoveries", obj.getInt("recovered"));
            builder.add("activeCases", obj.getInt("active"));
            builder.add("cases", obj.getInt("todayCases"));
            builder.add("deaths", obj.getInt("todayDeaths"));
            builder.add("recoveries", obj.getInt("todayRecovered"));
            builder.add("missing", false);
        } catch (Exception ignored) {
            builder.add("missing", true);
        }
        return builder.build();
    }
}
