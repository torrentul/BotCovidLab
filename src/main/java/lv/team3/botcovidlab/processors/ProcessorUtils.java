package lv.team3.botcovidlab.processors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class ProcessorUtils {

    // TODO Have to check this file for errors / exceptions

    // TODO Implement this better
    public static class DateStructure {
        private Calendar calendar;

        public DateStructure(String date) {
            this.calendar = new Calendar.Builder().build();
            String[] split = date.split("-");
            this.setYear(Integer.parseInt(split[0]));
            this.setMonth(Integer.parseInt(split[1]));
            this.setDay(Integer.parseInt(split[2].substring(0, 2)));
            split = date.split(":");
            this.setHour(Integer.parseInt(split[0].substring(split[0].length() - 2)));
            this.setMinute(Integer.parseInt(split[1]));
            this.setSecond(Integer.parseInt(split[2].substring(0, 2)));
        }

        public DateStructure(Date date) {
            this.calendar = new Calendar.Builder().build();
            this.calendar.setTime(date);
        }

        public int getYear() {
            return this.calendar.get(Calendar.YEAR);
        }

        public void setYear(int year) {
            this.calendar.set(Calendar.YEAR, year);
        }

        public int getMonth() {
            return this.calendar.get(Calendar.MONTH) + 1;
        }

        public void setMonth(int month) {
            this.calendar.set(Calendar.MONTH, month - 1);
        }

        public int getDay() {
            return this.calendar.get(Calendar.DAY_OF_MONTH);
        }

        public void setDay(int d) {
            this.calendar.set(Calendar.DAY_OF_MONTH, d);
        }

        public int getHour() {
            return this.calendar.get(Calendar.HOUR_OF_DAY);
        }

        public void setHour(int h) {
            this.calendar.set(Calendar.HOUR_OF_DAY, h);
        }

        public int getMinute() {
            return this.calendar.get(Calendar.MINUTE);
        }

        public void setMinute(int m) {
            this.calendar.set(Calendar.MINUTE, m);
        }

        public int getSecond() {
            return this.calendar.get(Calendar.SECOND);
        }

        public void setSecond(int s) {
            this.calendar.set(Calendar.SECOND, s);
        }

        public Date toDate() {
            return this.calendar.getTime();
        }

        @Override
        public String toString() {
            return String.format("%04d-%02d-%02dT%02d:%02d:%02dZ",
                    this.getYear(), this.getMonth(), this.getDay(),
                    this.getHour(), this.getMinute(), this.getSecond()
            );
        }
    }

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
        return base.getYear() <= test.getYear() &&
                base.getMonth() <= test.getMonth() &&
                (including ? base.getDay() <= test.getDay() : base.getDay() < test.getDay());
    }

    public static boolean isDateBefore(DateStructure test, DateStructure base, boolean including) {
        return base.getYear() >= test.getYear() &&
                base.getMonth() >= test.getMonth() &&
                (including ? base.getDay() >= test.getDay() : base.getDay() > test.getDay());
    }

    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean including) {
        return isDateInRange(testableDate, fromDate, toDate, including, including);
    }

    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean fromIncluding, boolean toIncluding) {
        boolean test = isDateAfter(testableDate, fromDate, fromIncluding) && isDateBefore(testableDate, toDate, toIncluding);
        return test;
    }

}