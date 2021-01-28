package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils;
import lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;

import javax.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Period;
import java.util.Date;
import java.util.Set;
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
    public static JsonArray jsonFromSource(DataSource source, String country, DateStructure from, DateStructure to) {
        switch (source) {
            case COVID_19_API:
                return getCovid19ApiResponse(country, from, to);
            case CORONA_LMAO_NINJA_API:
                return getCoronaLmaoNinjaApiResponse(country, from, to);
            default:
                return Json.createArrayBuilder().build();
        }
    }

    // TODO Documentation
    private static JsonArray getCovid19ApiResponse(String country, DateStructure from, DateStructure to) {
        String template = DataSource.COVID_19_API.getTemplate();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        try {
            from.setDay(from.getDay() - 1);
            String request = String.format(template, country, from.toString(), to.toString());
            JsonArray jsonArray = jsonArrayFromURL(new URL(request));
            for (int i = 0; i < jsonArray.size(); i++) {
                String dateString = jsonArray.getJsonObject(i).getString("Date");
                if (ProcessorUtils.isValidDateString(dateString)) {
                    DateStructure struct = new DateStructure(dateString);
                    if (ProcessorUtils.isDateInRange(struct, from, to, true)) {
                        arrayBuilder.add(jsonArray.getJsonObject(i));
                    }
                }
            }
        } catch (Exception e) {}
        JsonArray ret = arrayBuilder.build();
        System.out.println(ret);
        return ret;
    }

    // TODO Documentation
    private static JsonArray getCoronaLmaoNinjaApiResponse(String country, DateStructure from, DateStructure to) {
        String template = DataSource.CORONA_LMAO_NINJA_API.getTemplate();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        try {
            DateStructure currentDate = new DateStructure(new Date());
            long mils = Math.abs(from.toDate().getTime() - currentDate.toDate().getTime());
            long days = TimeUnit.DAYS.convert(mils, TimeUnit.MILLISECONDS);
            String request = String.format(template, country, days);
            JsonObject jsonObject = jsonObjectFromURL(new URL(request)).getJsonObject("timeline");
            JsonObject cases = jsonObject.getJsonObject("cases");
            JsonObject recoveries = jsonObject.getJsonObject("recovered");
            JsonObject deaths = jsonObject.getJsonObject("deaths");
            Set<String> keys = cases.keySet();
            for(String key : keys) {
                String dateString = String.format("%sT00:00:00Z", key.replaceAll("/", "-"));
                if(ProcessorUtils.isValidDateString(dateString)) {
                    DateStructure struct = new DateStructure(dateString);
                    if(ProcessorUtils.isDateInRange(struct, from, to, true)) {

                    }
                }
                System.out.println(key);
            }
//            for (int i = 0; i < jsonArray.size(); i++) {
//                String dateString = jsonArray.getJsonObject(i).getString("Date");
//                if (ProcessorUtils.isValidDateString(dateString)) {
//                    DateStructure struct = new DateStructure(dateString);
//                    if (ProcessorUtils.isDateInRange(struct, from, to, true)) {
//                        arrayBuilder.add(jsonArray.getJsonObject(i));
//                    }
//                }
//            }
        } catch (Exception e) {}
        JsonArray ret = arrayBuilder.build();
        System.out.println(ret);
        return ret;
    }
}
