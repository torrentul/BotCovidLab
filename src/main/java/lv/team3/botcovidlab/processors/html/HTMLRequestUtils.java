package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils;
import lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;

import javax.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class HTMLRequestUtils {

    // TODO Documentation
    public static String stringFromURL(URL url) {
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

    /**
     * @param url URL which responds with json array
     * @author Janis Valentinovics
     */
    public static JsonArray jsonArrayFromURL(URL url) {
        String ret = stringFromURL(url);
        String string = ret != null ? ret : "[]";
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    // TODO Documentation
    public static JsonObject jsonObjectFromURL(URL url) {
        String ret = stringFromURL(url);
        String string = (ret = stringFromURL(url)) != null ? ret : "{}";
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonObject array = reader.readObject();
        reader.close();
        return array;
    }

    // TODO Documentation
    public static JsonObject jsonFromSource(String country, DateStructure from, DateStructure to) {
        if(country == null || "world".equalsIgnoreCase(country)) {
            return getCoronaWorldwide(from, to);
        } else {
            return getCoronaSpecific(country, from, to);
        }
    }

    // TODO Documentation
    private static JsonObject getCoronaSpecific(String country, DateStructure from, DateStructure to) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            DateStructure currentDate = new DateStructure(new Date());
            long mils = Math.abs(from.toDate().getTime() - currentDate.toDate().getTime());
            long days = TimeUnit.DAYS.convert(mils, TimeUnit.MILLISECONDS) + 2;
            String request = String.format(DataSource.CORONA_SPECIFIC.getTemplate(), country, days);
            JsonObject jsonObject = jsonObjectFromURL(new URL(request));
            for (String key : jsonObject.getJsonObject("timeline").getJsonObject("cases").keySet()) {
                int[] parts = new int[3];
                for (int i = 0; i < 3; i++) {
                    parts[i] = Integer.parseInt(key.split("/")[i]);
                }
                String dateString = String.format("20%02d-%02d-%02dT00:00:00Z", parts[2], parts[0], parts[1]);
                if (ProcessorUtils.isValidDateString(dateString)) {
                    DateStructure struct = new DateStructure(dateString);
                    DateStructure struct2 = new DateStructure(struct.toDate());
                    struct2.setDay(struct2.getDay() - 1);
                    String previousKey = String.format("%d/%d/%d", struct2.getMonth(), struct2.getDay(), struct2.getYear() % 100);
                    if (ProcessorUtils.isDateInRange(struct, from, to, true)) {
                        objectBuilder.add(
                                struct.toString(),
                                HTMLResponseUtils.jsonObjectForSpecific(jsonObject, key, previousKey)
                        );
                    }
                }
            }
        } catch (Exception e) { /* TODO Exception */}
        return objectBuilder.build();
    }

    // TODO Documentation
    private static JsonObject getCoronaWorldwide(DateStructure from, DateStructure to) {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        try {
            DateStructure currentDate = new DateStructure(new Date());
            long mils = Math.abs(from.toDate().getTime() - currentDate.toDate().getTime());
            long days = TimeUnit.DAYS.convert(mils, TimeUnit.MILLISECONDS) + 2;
            String request = String.format(DataSource.CORONA_WORLDWIDE.getTemplate(), days);
            JsonObject jsonObject = jsonObjectFromURL(new URL(request));
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
                    String previousKey = String.format("%d/%d/%d", struct2.getMonth(), struct2.getDay(), struct2.getYear() % 100);
                    if (ProcessorUtils.isDateInRange(struct, from, to, true)) {
                        objectBuilder.add(
                                struct.toString(),
                                HTMLResponseUtils.jsonObjectForWorldwide(jsonObject, key, previousKey)
                        );
                    }
                }
            }
        } catch (Exception e) { /* TODO Exception */}
        return objectBuilder.build();
    }
}
