package lv.team3.botcovidlab.adapter.facebook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Utility that counts the specified dates and return them in string format
 *
 * @author Vladislavs Visnevskis
 */
public class DateUtility {

    private DateUtility() {
    }

    /**
     * Method that returns yesterdays date
     *
     * @return previous day's date in specified format
     * @author Vladislavs Visnevskis
     */
    public static String getYesterdayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    /**
     * Method that returns date of the day seven days ago
     *
     * @return seven days ago date in specified format
     * @author Vladislavs Visnevskis
     */
    public static String getSevenDaysAgoDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return dateFormat.format(cal.getTime());
    }

    /**
     * Method that returns date of the day thirty days ago
     *
     * @return thirty days ago date in specified format
     * @author Vladislavs Visnevskis
     */
    public static String getThirtyDaysAgoDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        return dateFormat.format(cal.getTime());
    }

}
