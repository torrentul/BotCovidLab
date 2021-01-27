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

    public static class DateStructure {
        private int year;
        private int month;
        private int day;
        private int h;
        private int m;
        private int s;
        private Calendar.Builder builder;

        public DateStructure(String date) {
            this.builder = new Calendar.Builder();
            String[] split = date.split("-");
            this.setYear(Integer.parseInt(split[0]));
            this.setMonth(Integer.parseInt(split[1]));
            this.setDay(Integer.parseInt(split[2].substring(0, 2)));
            split = date.split(":");
            this.setHour(Integer.parseInt(split[0].substring(split[0].length() - 2)));
            this.setMinute(Integer.parseInt(split[1]));
            this.setSecond(Integer.parseInt(split[2].substring(0, 2)));
        }

        public int getYear() { return year; }
        public void setYear(int year) {
            this.year = Math.max(1700, year);
            this.builder.set(Calendar.YEAR, year);
        }

        public int getMonth() { return month; }
        public void setMonth(int month) {
            this.month = Math.min(12, Math.max(1, month));
            this.builder.set(Calendar.MONTH, month - 1);
        }

        public int getDay() { return day; }
        public void setDay(int day) {
            this.day = Math.min(31, Math.max(1, day));
            this.builder.set(Calendar.DAY_OF_MONTH, day);
        }

        public int getHour() { return h; }
        public void setHour(int h) {
            this.h = Math.min(23, Math.max(0, h));
            this.builder.set(Calendar.HOUR_OF_DAY, h);
        }

        public int getMinute() { return m; }
        public void setMinute(int m) {
            this.m = Math.min(59, Math.max(0, m));
            this.builder.set(Calendar.MINUTE, m);
        }

        public int getSecond() { return s; }
        public void setSecond(int s) {
            this.s = Math.min(59,Math.max(0, s));
            this.builder.set(Calendar.SECOND, s);
        }

        public Date toDate() {
            return this.builder.build().getTime();
        }

        @Override
        public String toString() {
            return String.format("%04d-%02d-%02dT%02d:%02d:%02dZ", year, month, day, h, m, s);
        }
    }

    /* @param url URL returning json string
     * @author Janis Valentinovics */
    public static String stringFromURL(URL url) {
        String ret = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringWriter writer = new StringWriter();
            String line = "";
            while((line = reader.readLine()) != null) {
                writer.write(line);
            }
            reader.close();
            ret = writer.toString();

            writer.flush();
            writer.close();
        } catch (Exception e) { /* TODO Proper returns */ }
        return ret != null ? ret : "[]";
    }

    /* @param string Json string to be parsed into json array
     * @author Janis Valentinovics */
    public static JsonArray jsonArrayFromString(String string) {
        // TODO Catch invalid string exceptions
        JsonReader reader = Json.createReader(new StringReader(string));
        JsonArray array = reader.readArray();
        reader.close();
        return array;
    }

    public static boolean isValidDateString(String testableDate) {
        if(testableDate.matches("^((202[\\d])-[\\d]{2}-[\\d]{2}T[\\d]{2}:[\\d]{2}:[\\d]{2}Z)$")) {
            return testableDate.equals(new DateStructure(testableDate).toString());
        }
        return false;
    }

    public static boolean isDateAfter(String testableDate, String baseDate, boolean including) {
        DateStructure test = new DateStructure(testableDate);
        DateStructure base = new DateStructure(baseDate);
        return  base.getYear() <= test.getYear() &&
                base.getMonth() <= test.getMonth() &&
                (including ? base.getDay() <= test.getDay() : base.getDay() < test.getDay());
    }

    public static boolean isDateBefore(String testableDate, String baseDate, boolean including) {
        DateStructure test = new DateStructure(testableDate);
        DateStructure base = new DateStructure(baseDate);
        return  base.getYear() >= test.getYear() &&
                base.getMonth() >= test.getMonth() &&
                (including ? base.getDay() >= test.getDay() : base.getDay() > test.getDay());
    }

    public static boolean isDateInRange(String testableDate, String fromDate, String toDate, boolean including) {
        System.out.println(fromDate + " < " + testableDate + " < " + toDate);
        return isDateInRange(testableDate, fromDate, toDate, including, including);
    }

    public static boolean isDateInRange(String testableDate, String fromDate, String toDate, boolean fromIncluding, boolean toIncluding) {
        boolean test = isDateAfter(testableDate, fromDate, fromIncluding) && isDateBefore(testableDate, toDate, toIncluding);
        return test;
    }

}