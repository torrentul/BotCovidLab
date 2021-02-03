package lv.team3.botcovidlab.processors;

import lv.team3.botcovidlab.utils.DateUtils.DateStructure;

/**
 * Contains methods for CovidStatsProcessor data validation checking.<br>
 * {@link #isValidDateString(String)}<br>
 * {@link #isDateAfter(DateStructure, DateStructure, boolean)}<br>
 * {@link #isDateBefore(DateStructure, DateStructure, boolean)}<br>
 * {@link #isDateInRange(DateStructure, DateStructure, DateStructure, boolean)}<br>
 * {@link #isDateInRange(DateStructure, DateStructure, DateStructure, boolean, boolean)}
 */
public class ProcessorUtils {

    private ProcessorUtils() {
    }

    /**
     * Validates the <code>testableDate</code>, if it can be parsed into DateStructure object.
     *
     * @param testableDate String to be tested for specific formatting. "<code>YYY-MM-DD</code>T<code>HH:MM:SS</code>Z".
     * @return True if <code>testableDate</code> is valid, false if <code>testableDate</code>.
     * @author Janis Valentinovics.
     */
    public static boolean isValidDateString(String testableDate) {
        if (testableDate.matches("^((202[\\d])-[\\d]{2}-[\\d]{2}T[\\d]{2}:[\\d]{2}:[\\d]{2}Z)$")) {
            return testableDate.equals(new DateStructure(testableDate).toString());
        }
        return false;
    }

    /**
     * Tests if <code>test</code> date is after <code>base</code> date with possible <code>including</code> property.
     *
     * @param test      Date which will be tested against <code>base</code>.
     * @param base      Date to test <code>test</code> date against.
     * @param including If <code>true</code> uses <code>bigger or equals</code>, else <code>bigger</code>.
     * @return True if <code>test</code> date is after <code>base</code> date.
     * @author Janis Valentinovics.
     */
    public static boolean isDateAfter(DateStructure test, DateStructure base, boolean including) {
        if (base.getYear() < test.getYear()) return true;
        else if (base.getYear() == test.getYear()) {
            if (base.getMonth() < test.getMonth()) return true;
            else if (base.getMonth() == test.getMonth()) {
                return (including ? base.getDay() <= test.getDay() : base.getDay() < test.getDay());
            }
        }
        return false;
    }

    /**
     * Tests if <code>test</code> date is before <code>base</code> date with possible <code>including</code> property.
     *
     * @param test      Date which will be tested against <code>base</code>.
     * @param base      Date to test <code>test</code> date against.
     * @param including If <code>true</code> uses <code>smaller or equals</code>, else <code>smaller</code>.
     * @return True if <code>test</code> date is before <code>base</code> date.
     * @author Janis Valentinovics.
     */
    public static boolean isDateBefore(DateStructure test, DateStructure base, boolean including) {
        if (base.getYear() > test.getYear()) return true;
        else if (base.getYear() == test.getYear()) {
            if (base.getMonth() > test.getMonth()) return true;
            else if (base.getMonth() == test.getMonth()) {
                return (including ? base.getDay() >= test.getDay() : base.getDay() > test.getDay());
            }
        }
        return false;
    }

    /**
     * Checks if given date <code>testableDate</code> is in range of dates, starting from <code>fromDate</code> ending
     * ending with <code>toDate</code>. Including both if <code>including</code> is true.
     *
     * @param testableDate Testable date.
     * @param fromDate     Date from which range starts.
     * @param toDate       Date with which range ends.
     * @param including    If true, start and end will be [including], else both are (excluding).
     * @return True if <code>testableDate</code> is in range from <code>fromDate</code> to <code>toDate</code>.
     * @author Janis Valentinovics.
     */
    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean including) {
        return isDateInRange(testableDate, fromDate, toDate, including, including);
    }

    /**
     * Checks if given date <code>testableDate</code> is in range of dates, starting from <code>fromDate</code> ending
     * ending with <code>toDate</code>. Including <code>fromDate</code> if <code>fromIncluding</code> is true.
     * Including <code>toDate</code> if <code>toIncluding</code> is true.
     *
     * @param testableDate  Testable date.
     * @param fromDate      Date from which range starts.
     * @param toDate        Date with which range ends.
     * @param fromIncluding If true, <code>fromDate</code> will be [inclusive], else (exclusive).
     * @param toIncluding   If true, <code>toDate</code> will be [inclusive], else (exclusive).
     * @return True if <code>testableDate</code> is in range from <code>fromDate</code> to <code>toDate</code>.
     * @author Janis Valentinovics.
     */
    public static boolean isDateInRange(DateStructure testableDate, DateStructure fromDate, DateStructure toDate, boolean fromIncluding, boolean toIncluding) {
        return isDateAfter(testableDate, fromDate, fromIncluding) && isDateBefore(testableDate, toDate, toIncluding);
    }

}