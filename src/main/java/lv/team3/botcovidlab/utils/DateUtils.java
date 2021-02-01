package lv.team3.botcovidlab.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static class DateStructure {
        private final Calendar calendar;

        /**
         * @param date String in format "YYYY-MM-DDTHH:MM:SSZ"
         * @author Janis Valentinovics
         */
        public DateStructure(String date) {
            this.calendar = new Calendar.Builder().build();
            try {
                String[] split = date.split("-");
                this.setYear(Integer.parseInt(split[0]));
                this.setMonth(Integer.parseInt(split[1]));
                this.setDay(Integer.parseInt(split[2].substring(0, 2)));
                split = date.split(":");
                this.setHour(Integer.parseInt(split[0].substring(split[0].length() - 2)));
                this.setMinute(Integer.parseInt(split[1]));
                this.setSecond(Integer.parseInt(split[2].substring(0, 2)));
            } catch (Exception ignored) {
                // TODO Implement logger
                // Log error, passed string was invalid
            }
        }

        /**
         * @param date java.util.Date to DateStructure
         * @author Janis Valentinovics
         */
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

        /**
         * Turns DateStructure into java.util.Date
         * @return java.util.Date object
         * @author Janis Valentinovics
         */
        public Date toDate() {
            return this.calendar.getTime();
        }

        /**
         * @return String in format "YYYY-MM-DDTHH:MM:SSZ"
         * @author Janis Valentinovics
         */
        @Override
        public String toString() {
            return String.format("%04d-%02d-%02dT%02d:%02d:%02dZ",
                    this.getYear(), this.getMonth(), this.getDay(),
                    this.getHour(), this.getMinute(), this.getSecond()
            );
        }
    }
}
