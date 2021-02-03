package lv.team3.botcovidlab.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for date and time parsing and other functionality.
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * Structure used for easier date transformations.
     */
    public static class DateStructure {
        private final Calendar calendar;

        /**
         * @param date String in format "<code>YYY-MM-DD</code>T<code>HH:MM:SS</code>Z"
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
         * @param date {@link Date} to DateStructure
         * @author Janis Valentinovics
         */
        public DateStructure(Date date) {
            this.calendar = new Calendar.Builder().build();
            this.calendar.setTime(date);
        }

        /**
         * Basic getter, to get year of the date
         *
         * @return Year of the date
         */
        public int getYear() {
            return this.calendar.get(Calendar.YEAR);
        }

        /**
         * Basic setter, to set year of the date
         */
        public void setYear(int year) {
            this.calendar.set(Calendar.YEAR, year);
        }

        /**
         * Basic getter, to get month of the date
         *
         * @return Month of the date int [1-21]
         */
        public int getMonth() {
            return this.calendar.get(Calendar.MONTH) + 1;
        }

        /**
         * Basic setter, to set month of the date
         */
        public void setMonth(int month) {
            this.calendar.set(Calendar.MONTH, month - 1);
        }

        /**
         * Basic getter, to get day of the date
         *
         * @return Day of the date int [1-31]
         */
        public int getDay() {
            return this.calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * Basic setter, to set day of the date
         */
        public void setDay(int d) {
            this.calendar.set(Calendar.DAY_OF_MONTH, d);
        }

        /**
         * Basic getter, to get hour of the day
         *
         * @return Hour of the day int [0-23]
         */
        public int getHour() {
            return this.calendar.get(Calendar.HOUR_OF_DAY);
        }

        /**
         * Basic setter, to set hour of the date day time
         */
        public void setHour(int h) {
            this.calendar.set(Calendar.HOUR_OF_DAY, h);
        }

        /**
         * Basic getter, to get minutes in hour of the day
         *
         * @return Minutes in hour of the day int [0-59]
         */
        public int getMinute() {
            return this.calendar.get(Calendar.MINUTE);
        }

        /**
         * Basic setter, to set minute of the date day time
         */
        public void setMinute(int m) {
            this.calendar.set(Calendar.MINUTE, m);
        }

        /**
         * Basic getter, to get seconds in minute of the day
         *
         * @return Seconds in minute of the day int [0-59]
         */
        public int getSecond() {
            return this.calendar.get(Calendar.SECOND);
        }

        /**
         * Basic setter, to set second of the date day time
         */
        public void setSecond(int s) {
            this.calendar.set(Calendar.SECOND, s);
        }

        /**
         * Turns DateStructure into java.util.Date
         *
         * @return {@link Date}
         * @author Janis Valentinovics
         */
        public Date toDate() {
            return this.calendar.getTime();
        }

        /**
         * @return String in format "<code>YYY-MM-DD</code>T<code>HH:MM:SS</code>Z"
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
