package lv.team3.botcovidlab.processors.html;

import lv.team3.botcovidlab.processors.ProcessorUtils;
import lv.team3.botcovidlab.processors.ProcessorUtils.DateStructure;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

public class HTMLRequestUtils {

    /**
     * @param url URL which responds with json array
     * @author Janis Valentinovics
     */
    public static JsonArray jsonArrayFromURL(URL url) {
        // TODO Catch invalid string exceptions
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
        String string = ret != null ? ret : "[]";
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    public static JsonArray jsonFromSource(DataSource source, String country, DateStructure from, DateStructure to) {
        switch (source) {
            case COVID_19_API:
                return getCovid19ApiResponse(country, from, to);
            case TEST0_API:
                break;
            case TEST1_API:
                break;
            default:
                return Json.createArrayBuilder().build();
        }
        return null;
    }

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
        } catch (MalformedURLException e) {}
        JsonArray ret = arrayBuilder.build();
        System.out.println(ret);
        return ret;
    }

}
