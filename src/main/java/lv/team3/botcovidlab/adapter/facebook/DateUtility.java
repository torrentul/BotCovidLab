package lv.team3.botcovidlab.adapter.facebook;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtility {

    /**
     * @return previous day's date in specified format
     * @Author Vladislavs Višņevskis
     */
    public static String getYesterdayDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    /**
     * @return seven days ago date in specified format
     * @Author Vladislavs Višņevskis
     */
    public static String getSevenDaysAgoDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return dateFormat.format(cal.getTime());
    }

    /**
     * @return thirty days ago date in specified format
     * @Author Vladislavs Višņevskis
     */
    public static String getThirtyDaysAgoDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -31);
        return dateFormat.format(cal.getTime());
    }

}
