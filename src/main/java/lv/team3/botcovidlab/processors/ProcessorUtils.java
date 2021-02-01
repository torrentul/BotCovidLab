package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.utils.DateUtils.DateStructure;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

public class ProcessorUtils {

    // TODO Have to check this file for errors / exceptions

    /**
     * @param url URL returning json string
     * @author Janis Valentinovics
     */
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
        return ret != null ? ret : "[]";
    }

    /**
     * @param string Json string to be parsed into json array
     * @author Janis Valentinovics
     */
    public static JsonArray jsonArrayFromString(String string) {
        // TODO Catch invalid string exceptions
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    public static boolean isValidDateString(String testableDate) {
        if (testableDate.matches("^((202[\\d])-[\\d]{2}-[\\d]{2}T[\\d]{2}:[\\d]{2}:[\\d]{2}Z)$")) {
            String s = new DateStructure(testableDate).toString();
            return testableDate.equals(new DateStructure(testableDate).toString());
        }
        return false;
    }

    public static boolean isDateAfter(DateStructure test, DateStructure base, boolean including) {
        if(base.getYear() < test.getYear()) return true;
        else if(base.getYear() == test.getYear()) {
            if(base.getMonth() < test.getMonth()) return true;
            else if(base.getMonth() == test.getMonth()) {
                return (including ? base.getDay() <= test.getDay() : base.getDay() < test.getDay());
            }
        }
        return false;
    }

    public static boolean isDateBefore(DateStructure test, DateStructure base, boolean including) {
        if(base.getYear() > test.getYear()) return true;
        else if(base.getYear() == test.getYear()) {
            if(base.getMonth() > test.getMonth()) return true;
            else if(base.getMonth() == test.getMonth()) {
                return (including ? base.getDay() >= test.getDay() : base.getDay() > test.getDay());
            }
        }
        return false;
    }

    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean including) {
        return isDateInRange(testableDate, fromDate, toDate, including, including);
    }

    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean fromIncluding, boolean toIncluding) {
        return isDateAfter(testableDate, fromDate, fromIncluding) && isDateBefore(testableDate, toDate, toIncluding);
    }

}